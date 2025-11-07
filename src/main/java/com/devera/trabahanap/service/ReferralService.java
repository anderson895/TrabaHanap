package com.devera.trabahanap.service;

import com.devera.trabahanap.core.Referral;
import com.devera.trabahanap.system.Config;
import com.devera.trabahanap.system.FirebaseInitializer;
import com.devera.trabahanap.system.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Service for CRUD and search operations for Referrals using Firestore REST API.
 */
public class ReferralService {

    private static final Gson gson = new Gson();
    private final HttpClient http = HttpClient.newHttpClient();

    private static final String REFERRALS_COLLECTION = "referrals";
    private final String projectId;

    public ReferralService() {
        this.projectId = Config.get("firebase.projectId");
    }

    // --- CREATE ---
    public CompletableFuture<String> addReferral(Referral referral) {
        Objects.requireNonNull(referral, "referral must not be null");
        FirebaseInitializer.ensureInitialized();

        String url = String.format(
                "https://firestore.googleapis.com/v1/projects/%s/databases/(default)/documents/%s",
                projectId, REFERRALS_COLLECTION
        );

        JsonObject doc = new JsonObject();
        JsonObject fields = new JsonObject();

        addStringField(fields, "referredByUserId", referral.getReferredByUserId());
        addStringField(fields, "referredEmail", referral.getReferredEmail());
        addStringField(fields, "referredName", referral.getReferredName());
        addStringField(fields, "jobId", referral.getJobId());
        addStringField(fields, "status", referral.getStatus());

        JsonObject ts = new JsonObject();
        ts.addProperty("integerValue", Long.toString(referral.getTimestamp() > 0 ? referral.getTimestamp() : System.currentTimeMillis()));
        fields.add("timestamp", ts);

        doc.add("fields", fields);

        String body = gson.toJson(doc);

        return CompletableFuture.supplyAsync(() -> {
            try {
                String accessToken = obtainAccessTokenForFirestore();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .header("Authorization", "Bearer " + accessToken)
                        .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                        .build();

                HttpResponse<String> resp = http.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
                if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                    JsonObject json = gson.fromJson(resp.body(), JsonObject.class);
                    if (json != null && json.has("name")) {
                        String name = json.get("name").getAsString();
                        String[] parts = name.split("/");
                        String docId = parts[parts.length - 1];
                        referral.setReferralId(docId);
                        return docId;
                    }
                }
                throw new RuntimeException("Failed to add referral. HTTP " + resp.statusCode() + ": " + resp.body());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    // --- READ ---
    public CompletableFuture<List<Referral>> getAllReferrals() {
        FirebaseInitializer.ensureInitialized();

        String url = String.format(
                "https://firestore.googleapis.com/v1/projects/%s/databases/(default)/documents/%s",
                projectId, REFERRALS_COLLECTION
        );

        return CompletableFuture.supplyAsync(() -> {
            try {
                String accessToken = obtainAccessTokenForFirestore();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Authorization", "Bearer " + accessToken)
                        .GET()
                        .build();

                HttpResponse<String> resp = http.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
                if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                    List<Referral> out = new ArrayList<>();
                    JsonObject json = gson.fromJson(resp.body(), JsonObject.class);
                    if (json != null && json.has("documents")) {
                        JsonArray docs = json.getAsJsonArray("documents");
                        for (JsonElement el : docs) {
                            JsonObject doc = el.getAsJsonObject();
                            String name = doc.has("name") ? doc.get("name").getAsString() : null;
                            String docId = null;
                            if (name != null) {
                                String[] parts = name.split("/");
                                docId = parts[parts.length - 1];
                            }
                            JsonObject fields = doc.has("fields") ? doc.getAsJsonObject("fields") : null;
                            Map<String, Object> map = fieldsToMap(fields);
                            Referral r = fromMap(docId, map);
                            out.add(r);
                        }
                    }
                    out.sort(Comparator.comparingLong(Referral::getTimestamp).reversed());
                    return out;
                }
                throw new RuntimeException("Failed to fetch referrals. HTTP " + resp.statusCode() + ": " + resp.body());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<Referral> getReferralById(String referralId) {
        FirebaseInitializer.ensureInitialized();

        String url = String.format(
                "https://firestore.googleapis.com/v1/projects/%s/databases/(default)/documents/%s/%s",
                projectId, REFERRALS_COLLECTION, referralId
        );

        return CompletableFuture.supplyAsync(() -> {
            try {
                String accessToken = obtainAccessTokenForFirestore();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Authorization", "Bearer " + accessToken)
                        .GET()
                        .build();

                HttpResponse<String> resp = http.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
                if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                    JsonObject json = gson.fromJson(resp.body(), JsonObject.class);
                    JsonObject fields = json.has("fields") ? json.getAsJsonObject("fields") : null;
                    Map<String, Object> map = fieldsToMap(fields);
                    return fromMap(referralId, map);
                }
                return null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    // --- DELETE ---
    public CompletableFuture<Void> deleteReferral(String referralId) {
        FirebaseInitializer.ensureInitialized();

        String url = String.format(
                "https://firestore.googleapis.com/v1/projects/%s/databases/(default)/documents/%s/%s",
                projectId, REFERRALS_COLLECTION, referralId
        );

        return CompletableFuture.runAsync(() -> {
            try {
                String accessToken = obtainAccessTokenForFirestore();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Authorization", "Bearer " + accessToken)
                        .DELETE()
                        .build();

                HttpResponse<String> resp = http.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
                if (resp.statusCode() < 200 || resp.statusCode() >= 300) {
                    throw new RuntimeException("Failed to delete referral. HTTP " + resp.statusCode() + ": " + resp.body());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    // --- SEARCH ---
    public CompletableFuture<List<Referral>> searchReferralsByEmail(String email) {
        return getAllReferrals().thenApply(list -> {
            String lower = email.toLowerCase();
            List<Referral> filtered = new ArrayList<>();
            for (Referral r : list) {
                if (r.getReferredEmail() != null && r.getReferredEmail().toLowerCase().contains(lower)) {
                    filtered.add(r);
                }
            }
            return filtered;
        });
    }

    // -------------------
    // Helper methods
    // -------------------
    private static void addStringField(JsonObject parent, String fieldName, String value) {
        if (value == null) return;
        JsonObject v = new JsonObject();
        v.addProperty("stringValue", value);
        parent.add(fieldName, v);
    }

    private static Map<String, Object> fieldsToMap(JsonObject fields) {
        Map<String, Object> m = new HashMap<>();
        if (fields == null) return m;
        for (String key : fields.keySet()) {
            JsonObject v = fields.getAsJsonObject(key);
            if (v == null) continue;
            if (v.has("stringValue")) {
                m.put(key, v.get("stringValue").getAsString());
            } else if (v.has("integerValue")) {
                try {
                    m.put(key, Long.parseLong(v.get("integerValue").getAsString()));
                } catch (NumberFormatException e) {
                    m.put(key, 0L);
                }
            }
        }
        return m;
    }

    private static Referral fromMap(String id, Map<String, Object> map) {
        Referral r = new Referral();
        r.setReferralId(id);
        r.setReferredByUserId((String) map.get("referredByUserId"));
        r.setReferredEmail((String) map.get("referredEmail"));
        r.setReferredName((String) map.get("referredName"));
        r.setJobId((String) map.get("jobId"));
        r.setStatus((String) map.get("status"));
        Object ts = map.get("timestamp");
        r.setTimestamp(ts instanceof Number ? ((Number) ts).longValue() : 0L);
        return r;
    }

    private String obtainAccessTokenForFirestore() throws IOException {
        // Try admin/service account token first
        try {
            String token = FirebaseInitializer.getAccessToken();
            if (token != null && !token.isBlank()) return token;
        } catch (Throwable ignored) { }

        // Fallback: use user's idToken from SessionManager
        Optional<String> maybeIdToken = SessionManager.get().getIdToken();
        if (maybeIdToken.isPresent()) return maybeIdToken.get();

        throw new IOException("Cannot obtain Firestore access token");
    }
}
