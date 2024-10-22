API Documentation for User Rating App

The User Rating App allows users to rate and leave reviews for each other. 
The application stores user data and reviews using Firebase services.
Each time a review is added or deleted, the user's rating is automatically recalculated to reflect the most accurate average based on the total sum of their reviews.

Technologies Used:
Backend: Kotlin, Spring Boot, Spring Security
Database: Firebase Realtime Database
Authentication: Firebase Authentication
Dependency Management: Maven

Logging:
The service includes comprehensive logging using SLF4J, which records successful operations and any errors encountered during review and rating processes.

Endpoints: 

1. User Registration
Endpoint: /register
Method: POST
Description: Registers a new user with the provided email and password.
Request Body:
{
    "email": "user@example.com",
    "password": "userpassword"
}
Response:
Status Code: 201 Created - User registered successfully.

2. Assign Role to User
Endpoint: /users/{userId}
Method: POST
Description: Assigns a specific role (e.g., ADMIN) to a user identified by userId.
Path Variables:
userId: The ID of the user to whom the role will be assigned.
Request Parameters:
role: The role to assign to the user.
Response:
Status Code: 200 OK - Role assigned successfully.


3. Leave a Review
Endpoint: /users/{userId}/reviews
Method: POST
Description: Allows a user to leave a review for another user.
Path Variables:
userId: The ID of the user being reviewed.
Request Body:
{
    "rating": 5,
    "comment": "Excellent user!"
}
Response:
Status Code: 200 OK - Review submitted successfully.

5. Remove a Review
Endpoint: /users/{userId}/reviews/{reviewId}
Method: DELETE
Description: Deletes a review for a user. This action is restricted to users with the ADMIN role.
Path Variables:
userId: The ID of the user whose review is being deleted.
reviewId: The ID of the review to be deleted.
Response:
Status Code: 200 OK - Review removed successfully.
