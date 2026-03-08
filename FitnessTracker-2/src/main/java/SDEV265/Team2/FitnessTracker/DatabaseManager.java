package SDEV265.Team2.FitnessTracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseManager {

    // creates a file named 'Fitness.db' if one does not already exist
    
    private static final String URL = "jdbc:sqlite:Fitness.db";


    // Establishes a connection to the database.

    private Connection connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found: " + e.getMessage());
        }
        return DriverManager.getConnection(URL);
    }

    // creates necessary tables if they do not exist.

    public void initializeDatabase() {
       
        String sql1 = "CREATE TABLE IF NOT EXISTS fitness_goals ("
                   + " goal_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + " goal_name TEXT NOT NULL,"
                   + " goal_type TEXT NOT NULL,"
                   + " target_value TEXT NOT NULL,"
                   + " current_value TEXT NOT NULL,"
                   + " start_date TEXT NOT NULL,"
                   + " end_date TEXT NOT NULL,"
                   + " status TEXT NOT NULL"
                   + ");";

        String sql2 = "CREATE TABLE IF NOT EXISTS meals ("
                   + " meal_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + " meal_date TEXT NOT NULL,"
                   + " meal_type TEXT NOT NULL,"
                   + " food_name TEXT NOT NULL,"
                   + " calories TEXT NOT NULL,"
                   + " protien_g TEXT NOT NULL,"
                   + " carbs_g TEXT NOT NULL,"
                   + " fat_g TEXT NOT NULL,"
                   + " serving_size TEXT NOT NULL"
                   + ");";

        String sql3 = "CREATE TABLE IF NOT EXISTS workouts ("
                   + " workout_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + " workout_type TEXT NOT NULL,"
                   + " workout_date TEXT NOT NULL,"
                   + " duration_minutes TEXT NOT NULL,"
                   + " intensity_level TEXT NOT NULL,"
                   + " is_scheduled TEXT NOT NULL"
                   + ");";

        String sql4 = "CREATE TABLE IF NOT EXISTS sleep ("
                   + " sleep_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + " sleep_hours INTEGER NOT NULL,"
                   + " sleep_date TEXT NOT NULL,"
                   + " sleep_quality TEXT NOT NULL"
                   + ");";

        String sql5 = "CREATE TABLE IF NOT EXISTS hydration ("
                   + " hydration_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + " log_date TEXT NOT NULL,"
                   + " cups INTEGER NOT NULL"
                   + ");";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql1);
            stmt.execute(sql2);
            stmt.execute(sql3);
            stmt.execute(sql4);
            stmt.execute(sql5);
            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Init Error: " + e.getMessage());
        }
    }

    //Saves user goal

    public void saveUserGoal(String goal_name, String goal_type, String target_value, String current_value, String start_date, String end_date, String status) {
        String sql = "INSERT INTO fitness_goals(goal_name, goal_type, target_value, current_value, start_date, end_date, status) VALUES(?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, goal_name);
            pstmt.setString(2, goal_type);
            pstmt.setString(3, target_value);
            pstmt.setString(4, current_value);
            pstmt.setString(5, start_date);
            pstmt.setString(6, end_date);
            pstmt.setString(7, status);
            pstmt.executeUpdate();
            
            System.out.println("Data saved successfully!");
        } catch (SQLException e) {
            System.err.println("Insert Error: " + e.getMessage());
        }
    }

    //Saves user meal

    public void saveUserMeal(String meal_date, String meal_type, String meal_name, String calories, String protien_g, String carbs_g, String fat_g, String serving_size) {
        String sql = "INSERT INTO meals(meal_date, meal_type, meal_name, calories, protien_g, carbs_g, fat_g, serving_size) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, meal_date);
            pstmt.setString(2, meal_type);
            pstmt.setString(3, meal_name);
            pstmt.setString(4, calories);
            pstmt.setString(5, protien_g);
            pstmt.setString(6, carbs_g);
            pstmt.setString(7, fat_g);
            pstmt.setString(8, serving_size);
            pstmt.executeUpdate();
            
            System.out.println("Data saved successfully!");
        } catch (SQLException e) {
            System.err.println("Insert Error: " + e.getMessage());
        }
    }

    // save user hydration

    public void saveUserHydration(String log_date, int cups) {
        String sql = "INSERT INTO hydration(log_date, cups) VALUES(?, ?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, log_date);
            pstmt.setInt(2, cups);
            pstmt.executeUpdate();
            System.out.println("Hydration log saved successfully!");
        } catch (SQLException e) {
            System.err.println("Hydration Insert Error: " + e.getMessage());
        }
    }

    //Saves user workout

    public void saveUserWorkout(String workout_type, String workout_date, String duration_minutes, String intensity_level, String is_scheduled) {
        String sql = "INSERT INTO workouts(workout_type, workout_date, duration_minutes, intensity_level, is_scheduled) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, workout_type);
            pstmt.setString(2, workout_date);
            pstmt.setString(3, duration_minutes);
            pstmt.setString(4, intensity_level);
            pstmt.setString(5, is_scheduled);
            pstmt.executeUpdate();
            
            System.out.println("Data saved successfully!");
        } catch (SQLException e) {
            System.err.println("Insert Error: " + e.getMessage());
        }
    }

    //Saves user sleep

    public void saveUserSleep(String sleep_date, String sleep_duration, String sleep_quality) {
        String sql = "INSERT INTO sleep(sleep_date, sleep_duration, sleep_quality) VALUES(?, ?, ?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, sleep_date);
            pstmt.setString(2, sleep_duration);
            pstmt.setString(3, sleep_quality);
            pstmt.executeUpdate();
            
            System.out.println("Data saved successfully!");
        } catch (SQLException e) {
            System.err.println("Insert Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        db.initializeDatabase();
    }

    //Update existing goals
    
    public void updateGoal(int goal_id, String goal_name, String goal_type, String target_value, String current_value, String start_date, String end_date, String status)
    {
        String sql = "UPDATE fitness_goals SET goal_name = ?, goal_type = ?, target_value = ?, current_value = ?, start_date = ?, end_date = ?, status = ? WHERE goal_id = ?";

        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, goal_name);
            pstmt.setString(2, goal_type);
            pstmt.setString(3, target_value);
            pstmt.setString(4, current_value);
            pstmt.setString(5, start_date);
            pstmt.setString(6, end_date);
            pstmt.setString(7, status);
            pstmt.setInt(8, goal_id);
            pstmt.executeUpdate();
            System.out.println("Goal updated successfully!");

        } catch (SQLException e)
            {
                System.err.println("Update Error: " + e.getMessage());
            }
    }

    //Update existing meals

    public void updateMeal(int meal_id, String meal_date, String meal_type, String food_name, String calories, String protien_g, String carbs_g, String fat_g, String serving_size)
    {
        String sql = "UPDATE meals SET meal_date = ?, meal_type = ?, food_name = ?, calories = ?, protien_g = ?, carbs_g = ?, fat_g = ?, serving_size = ? WHERE meal_id = ?";

        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, meal_date);
            pstmt.setString(2, meal_type);
            pstmt.setString(3, food_name);
            pstmt.setString(4, calories);
            pstmt.setString(5, protien_g);
            pstmt.setString(6, carbs_g);
            pstmt.setString(7, fat_g);
            pstmt.setString(8, serving_size);
            pstmt.setInt(9, meal_id);
            pstmt.executeUpdate();
            System.out.println("Meal updated successfully!");

        } catch (SQLException e)
            {
                System.err.println("Update Error: " + e.getMessage());
            }
    }

    //Update existing workouts

    public void updateWorkout(int workout_id, String workout_type, String workout_date, String duration_minutes, String intensity_level, String is_scheduled)
    {
        String sql = "UPDATE workouts SET workout_type = ?, workout_date = ?, duration_minutes = ?, intensity_level = ?, is_scheduled = ? WHERE workout_id = ?";

        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, workout_type);
            pstmt.setString(2, workout_date);
            pstmt.setString(3, duration_minutes);
            pstmt.setString(4, intensity_level);
            pstmt.setString(5, is_scheduled);
            pstmt.setInt(6, workout_id);
            pstmt.executeUpdate();
            System.out.println("Workout updated successfully!");

        } catch (SQLException e)
            {
                System.err.println("Update Error: " + e.getMessage());
            }
    }

    //Update existing sleep log

    public void updateSleep(int sleep_id, String sleep_date, String sleep_duration, String sleep_quality)
    {
        String sql = "UPDATE sleep_logs SET sleep_date = ?, sleep_duration = ?, sleep_quality = ? WHERE sleep_id = ?";

        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, sleep_date);
            pstmt.setString(2, sleep_duration);
            pstmt.setString(3, sleep_quality);
            pstmt.setInt(4, sleep_id);
            pstmt.executeUpdate();
            System.out.println("Sleep log updated successfully!");

        } catch (SQLException e)
            {
                System.err.println("Update Error: " + e.getMessage());
            }
    }

    //Update existing hydration log

    public void updateHydration(int hydration_id, String hydration_date, String cups)
    {
        String sql = "UPDATE hydration_logs SET hydration_date = ?, cups = ? WHERE hydration_id = ?";

        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, hydration_date);
            pstmt.setString(2, cups);
            pstmt.setInt(3, hydration_id);
            pstmt.executeUpdate();
            System.out.println("Hydration log updated successfully!");

        } catch (SQLException e)
            {
                System.err.println("Update Error: " + e.getMessage());
            }
    }

    //Delete old goals

    public void deleteGoal(int goal_id)
    {
        String sql = "DELETE FROM fitness_goals WHERE goal_id = ?";

        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, goal_id);
            pstmt.executeUpdate();
            System.out.println("Goal deleted successfully!");

        } catch (SQLException e)
            {
                System.err.println("Delete Error: " + e.getMessage());
            }
    }

    //Delete old meals

    public void deleteMeal(int meal_id)
    {
        String sql = "DELETE FROM meals WHERE meal_id = ?";

        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, meal_id);
            pstmt.executeUpdate();
            System.out.println("Meal deleted successfully!");

        } catch (SQLException e)
            {
                System.err.println("Delete Error: " + e.getMessage());
            }
    }

    //Delete old workouts

    public void deleteWorkout(int workout_id)
    {
        String sql = "DELETE FROM workouts WHERE workout_id = ?";

        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, workout_id);
            pstmt.executeUpdate();
            System.out.println("Workout deleted successfully!");

        } catch (SQLException e)
            {
                System.err.println("Delete Error: " + e.getMessage());
            }
    }

    //Delete old sleep logs

    public void deleteSleep(int sleep_id)
    {
        String sql = "DELETE FROM sleep_logs WHERE sleep_id = ?";

        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, sleep_id);
            pstmt.executeUpdate();
            System.out.println("Sleep log deleted successfully!");

        } catch (SQLException e)
            {
                System.err.println("Delete Error: " + e.getMessage());
            }
    }

    //Delete old hydration logs

    public void deleteHydration(int hydration_id)
    {
        String sql = "DELETE FROM hydration_logs WHERE hydration_id = ?";

        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, hydration_id);
            pstmt.executeUpdate();
            System.out.println("Hydration log deleted successfully!");

        } catch (SQLException e)
            {
                System.err.println("Delete Error: " + e.getMessage());
            }
    }

    //get goal - returns a string input is goal type

    public String getGoal(String goal_type)
    {
        String sql = "SELECT * FROM goals WHERE goal_type = ?";

        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, goal_type);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return "Goal: " +
                    rs.getString("goal_type") + " = " +
                    rs.getDouble("target_value") + " " +
                    rs.getString("unit") +
                    " (Deadline: " + rs.getString("deadline") + ")";
            }

        } catch (SQLException e)
            {
                System.err.println("Retrieval Error: " + e.getMessage());
            }

        return "No goal found for type: " + goal_type;
    }

    //get workout - returns a string input is date

    public String getWorkout(String workout_date)
    {
        String sql = "SELECT * FROM workouts WHERE workout_date = ?";

        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, workout_date);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return "Workout: " +
                    rs.getString("workout_type") + ", " +
                    rs.getInt("duration_minutes") + " mins, " +
                    "Intensity: " + rs.getString("intensity_level") + ".";
            }

        } catch (SQLException e)
            {
                System.err.println("Retrieval Error: " + e.getMessage());
            }

        return "No workout found for " + workout_date;
    }

    //get meal - returns a string input is date

    public String getMeal(String meal_date)
    {
        String sql = "SELECT * FROM meals WHERE meal_date = ?";

        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, meal_date);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return "Meal: " +
                    rs.getString("meal_type") + ", " +
                    rs.getInt("calories") + " calories.";
            }

        } catch (SQLException e)
            {
                System.err.println("Retrieval Error: " + e.getMessage());
            }

        return "No meal found for " + meal_date;
    }

    //get hydration - returns a string input is date

    public String getHydration(String hydration_date)
    {
        String sql = "SELECT * FROM hydration WHERE log_date = ?";

        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, hydration_date);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return "Hydration: " +
                    rs.getString("cups") + " cups.";
            }

        } catch (SQLException e)
            {
                System.err.println("Retrieval Error: " + e.getMessage());
            }

        return "No hydration record found for " + hydration_date;
    }

    //get sleep - returns a string input is date

    public String getSleep(String sleep_date)
    {
        String sql = "SELECT * FROM sleep WHERE sleep_date = ?";

        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, sleep_date);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return "Sleep: " +
                    rs.getInt("hours_slept") + " hours, Quality: " +
                    rs.getString("sleep_quality") + ".";
            }

        } catch (SQLException e)
            {
                System.err.println("Retrieval Error: " + e.getMessage());
            }

        return "No sleep record found for " + sleep_date;
    }

    //get all goals - returns a list of maps

    public List<Map<String, String>> getAllGoals() {
        List<Map<String, String>> goals = new ArrayList<>();
        String sql = "SELECT * FROM fitness_goals";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, String> goal = new HashMap<>();
                goal.put("goal_name", rs.getString("goal_name"));
                goal.put("goal_type", rs.getString("goal_type"));
                goal.put("target_value", rs.getString("target_value"));
                goal.put("current_value", rs.getString("current_value"));
                goal.put("start_date", rs.getString("start_date"));
                goal.put("end_date", rs.getString("end_date"));
                goal.put("status", rs.getString("status"));
                goals.add(goal);
            }

        } catch (SQLException e) {
            System.err.println("Retrieval Error: " + e.getMessage());
        }

        return goals;
    }

    // Returns the count of all workouts logged.

    public int getWorkoutCount() {
        String sql = "SELECT COUNT(*) FROM workouts";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting workout count: " + e.getMessage());
        }
        return 0;
    }

    // Returns the count of all meals logged.

    public int getMealCount() {
        String sql = "SELECT COUNT(*) FROM meals";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting meal count: " + e.getMessage());
        }
        return 0;
    }

    // Returns the count of all sleep entries logged.

    public int getSleepCount() {
        String sql = "SELECT COUNT(*) FROM sleep";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting sleep count: " + e.getMessage());
        }
        return 0;
    }

    // Returns the total sleep hours logged.

    public int getTotalSleepHours() {
        String sql = "SELECT SUM(sleep_hours) FROM sleep";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int total = rs.getInt(1);
                return total == 0 && rs.wasNull() ? 0 : total;
            }
        } catch (SQLException e) {
            System.err.println("Error getting total sleep hours: " + e.getMessage());
        }
        return 0;
    }

    // Returns the total cups of water logged.

    public int getTotalCups() {
        String sql = "SELECT SUM(cups) FROM hydration";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int total = rs.getInt(1);
                return total == 0 && rs.wasNull() ? 0 : total;
            }
        } catch (SQLException e) {
            System.err.println("Error getting total cups: " + e.getMessage());
        }
        return 0;
    }

    // Returns the count of all goals.

    public int getGoalsCount() {
        String sql = "SELECT COUNT(*) FROM fitness_goals";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting goals count: " + e.getMessage());
        }
        return 0;
    }

    // Returns the most recent workout entry.

    public Map<String, String> getRecentWorkout() {
        String sql = "SELECT * FROM workouts ORDER BY workout_date DESC LIMIT 1";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                Map<String, String> workout = new HashMap<>();
                workout.put("workout_type", rs.getString("workout_type"));
                workout.put("workout_date", rs.getString("workout_date"));
                return workout;
            }
        } catch (SQLException e) {
            System.err.println("Error getting recent workout: " + e.getMessage());
        }
        return new HashMap<>();
    }

    // Returns the most recent meal entry.

    public Map<String, String> getRecentMeal() {
        String sql = "SELECT * FROM meals ORDER BY meal_date DESC LIMIT 1";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                Map<String, String> meal = new HashMap<>();
                meal.put("food_name", rs.getString("food_name"));
                meal.put("meal_type", rs.getString("meal_type"));
                meal.put("meal_date", rs.getString("meal_date"));
                return meal;
            }
        } catch (SQLException e) {
            System.err.println("Error getting recent meal: " + e.getMessage());
        }
        return new HashMap<>();
    }

    // Returns the most recent sleep entry.

    public Map<String, String> getRecentSleep() {
        String sql = "SELECT * FROM sleep ORDER BY sleep_date DESC LIMIT 1";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                Map<String, String> sleep = new HashMap<>();
                sleep.put("sleep_date", rs.getString("sleep_date"));
                return sleep;
            }
        } catch (SQLException e) {
            System.err.println("Error getting recent sleep: " + e.getMessage());
        }
        return new HashMap<>();
    }

    // Returns the most recent goal.

    public Map<String, String> getRecentGoal() {
        String sql = "SELECT * FROM fitness_goals ORDER BY start_date DESC LIMIT 1";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                Map<String, String> goal = new HashMap<>();
                goal.put("goal_name", rs.getString("goal_name"));
                goal.put("start_date", rs.getString("start_date"));
                return goal;
            }
        } catch (SQLException e) {
            System.err.println("Error getting recent goal: " + e.getMessage());
        }
        return new HashMap<>();
    }

    // Updates goal progress based on related table data (workouts, meals, sleep, hydration).

    public void updateGoalProgress(int goal_id, String goal_type) {
        String currentValue = "";

        if ("Strength".equalsIgnoreCase(goal_type) || "Endurance".equalsIgnoreCase(goal_type) || 
            "Flexibility".equalsIgnoreCase(goal_type)) {
            // Count workouts of this type
            String sql = "SELECT COUNT(*) FROM workouts WHERE workout_type = ?";
            try (Connection conn = this.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, goal_type);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    currentValue = String.valueOf(rs.getInt(1));
                }
            } catch (SQLException e) {
                System.err.println("Error counting workouts: " + e.getMessage());
            }
        } else if ("Weight".equalsIgnoreCase(goal_type)) {
            // Sum meals calories (as proxy for weight tracking)
            String sql = "SELECT SUM(CAST(calories AS INTEGER)) FROM meals";
            try (Connection conn = this.connect();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    currentValue = String.valueOf(rs.getInt(1));
                }
            } catch (SQLException e) {
                System.err.println("Error summing calories: " + e.getMessage());
            }
        }

        // Update the goal with calculated current value
        if (!currentValue.isEmpty()) {
            String updateSql = "UPDATE fitness_goals SET current_value = ? WHERE goal_id = ?";
            try (Connection conn = this.connect();
                 PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                pstmt.setString(1, currentValue);
                pstmt.setInt(2, goal_id);
                pstmt.executeUpdate();
                System.out.println("Goal progress updated to: " + currentValue);
            } catch (SQLException e) {
                System.err.println("Error updating goal progress: " + e.getMessage());
            }
        }
    }

    // Calculates total workouts logged for a specific type.

    public int getTotalWorkoutsByType(String workoutType) {
        String sql = "SELECT COUNT(*) FROM workouts WHERE workout_type = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, workoutType);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting workout count by type: " + e.getMessage());
        }
        return 0;
    }

    // Calculates total calories logged.

    public int getTotalCalories() {
        String sql = "SELECT SUM(CAST(calories AS INTEGER)) FROM meals";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int total = rs.getInt(1);
                return total == 0 && rs.wasNull() ? 0 : total;
            }
        } catch (SQLException e) {
            System.err.println("Error getting total calories: " + e.getMessage());
        }
        return 0;
    }

    // Calculates total workout duration in minutes.

    public int getTotalWorkoutDuration() {
        String sql = "SELECT SUM(CAST(duration_minutes AS INTEGER)) FROM workouts";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int total = rs.getInt(1);
                return total == 0 && rs.wasNull() ? 0 : total;
            }
        } catch (SQLException e) {
            System.err.println("Error getting total workout duration: " + e.getMessage());
        }
        return 0;
    }

    // Calculates total protein logged in grams.

    public int getTotalProtein() {
        String sql = "SELECT SUM(CAST(protien_g AS INTEGER)) FROM meals";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int total = rs.getInt(1);
                return total == 0 && rs.wasNull() ? 0 : total;
            }
        } catch (SQLException e) {
            System.err.println("Error getting total protein: " + e.getMessage());
        }
        return 0;
    }

    // Retrieves all active goals (status = "Active").

    public List<Map<String, String>> getActiveGoals() {
        List<Map<String, String>> goals = new ArrayList<>();
        String sql = "SELECT * FROM fitness_goals WHERE status = 'Active'";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, String> goal = new HashMap<>();
                goal.put("goal_id", String.valueOf(rs.getInt("goal_id")));
                goal.put("goal_name", rs.getString("goal_name"));
                goal.put("goal_type", rs.getString("goal_type"));
                goal.put("target_value", rs.getString("target_value"));
                goal.put("current_value", rs.getString("current_value"));
                goal.put("start_date", rs.getString("start_date"));
                goal.put("end_date", rs.getString("end_date"));
                goal.put("status", rs.getString("status"));
                goals.add(goal);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving active goals: " + e.getMessage());
        }

        return goals;
    }

    // Updates all active goals with current progress from related tables.

    public void updateAllGoalProgress() {
        List<Map<String, String>> activeGoals = getActiveGoals();
        for (Map<String, String> goal : activeGoals) {
            int goalId = Integer.parseInt(goal.get("goal_id"));
            String goalType = goal.get("goal_type");
            updateGoalProgress(goalId, goalType);
        }
        System.out.println("All active goals updated with current progress.");
    }

}
// Functions Still Needed:
// - Calculate progress towards goals based on meals and workouts