package com.codewithngoc.day07apporderfood.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codewithngoc.day07apporderfood.R;
import com.codewithngoc.day07apporderfood.model.FoodItem;
import com.codewithngoc.day07apporderfood.model.IngredientItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class FoodViewModel extends ViewModel {
    private final MutableLiveData<List<FoodItem>> visibleFoods = new MutableLiveData<>();
    private final MutableLiveData<FoodItem> selectedFood = new MutableLiveData<>();
    private final List<FoodItem> allFoods = createFoods();
    private String selectedCategory = "Breakfast";
    private String searchQuery = "";

    public FoodViewModel() {
        applyFilters();
        if (!allFoods.isEmpty()) {
            selectedFood.setValue(allFoods.get(0));
        }
    }

    public LiveData<List<FoodItem>> getVisibleFoods() {
        return visibleFoods;
    }

    public LiveData<FoodItem> getSelectedFood() {
        return selectedFood;
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void selectCategory(String category) {
        selectedCategory = category;
        applyFilters();
    }

    public void updateSearchQuery(String query) {
        searchQuery = query == null ? "" : query.trim();
        applyFilters();
    }

    public void selectFood(FoodItem foodItem) {
        if (foodItem != null) {
            selectedFood.setValue(foodItem);
        }
    }

    public void toggleFavorite() {
        FoodItem current = selectedFood.getValue();
        if (current == null) {
            return;
        }

        current.setFavorite(!current.isFavorite());
        selectedFood.setValue(current);
        applyFilters();
    }

    private void applyFilters() {
        List<FoodItem> filtered = new ArrayList<>();
        String normalizedQuery = searchQuery.toLowerCase(Locale.getDefault());

        for (FoodItem food : allFoods) {
            boolean matchesCategory = food.getCategory().equalsIgnoreCase(selectedCategory);
            boolean matchesQuery = normalizedQuery.isEmpty()
                    || food.getTitle().toLowerCase(Locale.getDefault()).contains(normalizedQuery);

            if (matchesCategory && matchesQuery) {
                filtered.add(food);
            }
        }

        visibleFoods.setValue(filtered);
    }

    private List<FoodItem> createFoods() {
        List<FoodItem> items = new ArrayList<>();

        items.add(new FoodItem(
                1,
                "Telor ceplok",
                "Breakfast",
                9,
                4.3,
                "Simple sunny-side eggs with chili flakes and fresh herbs for a quick breakfast.",
                "Easy",
                2,
                "🍳",
                "🍳",
                R.color.surface_muted,
                Arrays.asList(
                        new IngredientItem("Egg", "2 pcs", "🥚", R.color.surface_muted, Arrays.asList("Protein", "Fat")),
                        new IngredientItem("Chili flakes", "1 tsp", "🌶️", R.color.tag_bg_two, Arrays.asList("Spicy"))
                ),
                false
        ));

        items.add(new FoodItem(
                2,
                "Salad vegetarian",
                "Lunch",
                10,
                4.3,
                "Crisp vegetables tossed with citrus dressing, nuts, and creamy avocado.",
                "Easy",
                2,
                "🥗",
                "🥗",
                R.color.accent_green,
                Arrays.asList(
                        new IngredientItem("Avocado", "1 fruit", "🥑", R.color.accent_green, Arrays.asList("Healthy Fat")),
                        new IngredientItem("Carrot", "120 gr", "🥕", R.color.tag_bg_two, Arrays.asList("Fiber"))
                ),
                false
        ));

        items.add(new FoodItem(
                3,
                "Toast with egg",
                "Breakfast",
                30,
                4.3,
                "Golden toast layered with creamy eggs and pepper for a filling brunch plate.",
                "Medium",
                2,
                "🍞",
                "🍞",
                R.color.surface_muted,
                Arrays.asList(
                        new IngredientItem("Bread", "2 slices", "🍞", R.color.surface_muted, Arrays.asList("Carb")),
                        new IngredientItem("Egg cream", "150 gr", "🥚", R.color.tag_bg_three, Arrays.asList("Protein"))
                ),
                true
        ));

        items.add(new FoodItem(
                4,
                "Salmon sauce",
                "Dinner",
                45,
                4.3,
                "Pan-seared salmon finished with a silky lemon butter sauce and herbs.",
                "Medium",
                4,
                "🐟",
                "🍣",
                R.color.accent_green,
                Arrays.asList(
                        new IngredientItem("Salmon", "300 gr", "🐟", R.color.surface_muted, Arrays.asList("Protein", "Fat")),
                        new IngredientItem("Butter", "30 gr", "🧈", R.color.tag_bg_two, Arrays.asList("Fat"))
                ),
                false
        ));

        items.add(new FoodItem(
                5,
                "Mushroom soup",
                "Dinner",
                15,
                4.3,
                "Creamy mushroom soup with garlic, thyme, and cracked black pepper.",
                "Easy",
                3,
                "🍲",
                "🍲",
                R.color.surface_muted,
                Arrays.asList(
                        new IngredientItem("Mushroom", "200 gr", "🍄", R.color.surface_muted, Arrays.asList("Fiber")),
                        new IngredientItem("Cream", "100 ml", "🥛", R.color.tag_bg_three, Arrays.asList("Creamy"))
                ),
                false
        ));

        items.add(new FoodItem(
                6,
                "Gourmet dessert",
                "Breakfast",
                25,
                4.3,
                "Elegant layered mousse cake with fresh fruit toppings, including blueberries and kiwi.",
                "Medium",
                4,
                "🍰",
                "🍰",
                R.color.tag_bg_three,
                Arrays.asList(
                        new IngredientItem("Salmon", "300 gr", "🍣", R.color.surface_muted, Arrays.asList("Protein", "Fat", "Fresh")),
                        new IngredientItem("Blueberry", "80 gr", "🫐", R.color.tag_bg_three, Arrays.asList("Fruit", "Fresh")),
                        new IngredientItem("Kiwi", "1 fruit", "🥝", R.color.accent_green, Arrays.asList("Vitamin C"))
                ),
                true
        ));

        items.add(new FoodItem(
                7,
                "Berry pudding",
                "Snack",
                12,
                4.4,
                "Soft pudding topped with berry compote for a quick sweet snack.",
                "Easy",
                1,
                "🍮",
                "🍮",
                R.color.tag_bg_three,
                Arrays.asList(
                        new IngredientItem("Milk", "150 ml", "🥛", R.color.tag_bg_three, Arrays.asList("Creamy")),
                        new IngredientItem("Berry", "50 gr", "🫐", R.color.tag_bg_two, Arrays.asList("Fruit"))
                ),
                false
        ));

        return items;
    }
}
