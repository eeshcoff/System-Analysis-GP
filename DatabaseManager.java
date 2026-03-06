package SDEV265.Team2.FitnessTracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    // The connection URL (this creates a file named 'Fitness.db')
    private static final String URL = "jdbc:sqlite:Fitness.db";

    /**
     * Establishes a connection to the database.
     */
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /**
     * Initializes the database by creating necessary tables.
     */
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
                   + " notes TEXT NOT NULL,"
                   + " is_scheduled TEXT NOT NULL"
                   + ");";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql1);
            stmt.execute(sql2);
            stmt.execute(sql3);
            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Init Error: " + e.getMessage());
        }
    }

    /**
     * Logic to take data (from front end) and save to SQLite.
     * Uses PreparedStatement to prevent SQL Injection.
     */
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

    public void saveUserWorkout(String workout_type, String workout_date, String duration_minutes, String intensity_level, String notes, String is_scheduled) {
        String sql = "INSERT INTO workouts(workout_type, workout_date, duration_minutes, intensity_level, notes, is_scheduled) VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, workout_type);
            pstmt.setString(2, workout_date);
            pstmt.setString(3, duration_minutes);
            pstmt.setString(4, intensity_level);
            pstmt.setString(5, notes);
            pstmt.setString(6, is_scheduled);
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

    public void updateWorkout(int workout_id, String workout_type, String workout_date, String duration_minutes, String intensity_level, String notes, String is_scheduled)
    {
        String sql = "UPDATE workouts SET workout_type = ?, workout_date = ?, duration_minutes = ?, intensity_level = ?, notes = ?, is_scheduled = ? WHERE workout_id = ?";

        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, workout_type);
            pstmt.setString(2, workout_date);
            pstmt.setString(3, duration_minutes);
            pstmt.setString(4, intensity_level);
            pstmt.setString(5, notes);
            pstmt.setString(6, is_scheduled);
            pstmt.setInt(7, workout_id);
            pstmt.executeUpdate();
            System.out.println("Workout updated successfully!");

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

    //get Goal - returns a string input is date

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
                    "Intensity: " + rs.getString("intensity_level") + ", " +
                    "Notes: " + rs.getString("notes");
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
                    rs.getInt("calories") + " calories, " +
                    "Notes: " + rs.getString("notes");
            }

        } catch (SQLException e)
            {
                System.err.println("Retrieval Error: " + e.getMessage());
            }

        return "No meal found for " + meal_date;
    }

}

// Functions Still Needed:
// - Send data to front end for display Abdallah  3 / 5
// - Calculate progress towards goals based on meals and workouts 3 / 7