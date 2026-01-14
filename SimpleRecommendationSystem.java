import java.util.*;

public class SimpleRecommendationSystem {

    // User -> (Item -> Rating)
    static Map<Integer, Map<Integer, Double>> userRatings = new HashMap<>();

    public static void main(String[] args) {
        // Sample data setup
        addRating(1, 101, 4.5);
        addRating(1, 102, 3.0);
        addRating(1, 103, 5.0);

        addRating(2, 101, 5.0);
        addRating(2, 102, 2.5);
        addRating(2, 104, 4.0);  // new item 104 rated by user 2

        addRating(3, 101, 2.0);
        addRating(3, 103, 4.0);
        addRating(3, 104, 3.5);  // new item 104 rated by user 3

        addRating(4, 102, 4.5);
        addRating(4, 103, 1.0);
        addRating(4, 105, 5.0);  // new item 105 rated by user 4

        // Recommend 3 items for user 1
        List<Integer> recommendations = recommendItems(1, 3);

        System.out.println("Recommended items for user 1: " + recommendations);
    }

    // Add or update a rating
    static void addRating(int userId, int itemId, double rating) {
        userRatings.putIfAbsent(userId, new HashMap<>());
        userRatings.get(userId).put(itemId, rating);
    }

    // Calculate cosine similarity between two users
    static double similarity(int user1, int user2) {
        Map<Integer, Double> ratings1 = userRatings.get(user1);
        Map<Integer, Double> ratings2 = userRatings.get(user2);

        Set<Integer> commonItems = new HashSet<>(ratings1.keySet());
        commonItems.retainAll(ratings2.keySet());

        if (commonItems.isEmpty()) return 0.0;

        double dotProduct = 0, norm1 = 0, norm2 = 0;
        for (int item : commonItems) {
            dotProduct += ratings1.get(item) * ratings2.get(item);
        }

        for (double r : ratings1.values()) {
            norm1 += r * r;
        }
        for (double r : ratings2.values()) {
            norm2 += r * r;
        }

        if (norm1 == 0 || norm2 == 0) return 0.0;

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    // Recommend top N items for a user
    static List<Integer> recommendItems(int userId, int topN) {
        Map<Integer, Double> scores = new HashMap<>();
        Map<Integer, Double> targetUserRatings = userRatings.get(userId);

        for (int otherUser : userRatings.keySet()) {
            if (otherUser == userId) continue;
            double sim = similarity(userId, otherUser);
            if (sim <= 0) continue;

            for (Map.Entry<Integer, Double> entry : userRatings.get(otherUser).entrySet()) {
                int item = entry.getKey();

                // Recommend only items not rated by target user
                if (!targetUserRatings.containsKey(item)) {
                    scores.put(item, scores.getOrDefault(item, 0.0) + sim * entry.getValue());
                }
            }
        }

        // Sort items by weighted score descending
        List<Map.Entry<Integer, Double>> scoredItems = new ArrayList<>(scores.entrySet());
        scoredItems.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        // Extract top N items
        List<Integer> recommendations = new ArrayList<>();
        for (int i = 0; i < Math.min(topN, scoredItems.size()); i++) {
            recommendations.add(scoredItems.get(i).getKey());
        }

        return recommendations;
    }
}
