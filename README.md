## Blog Post Features

The heart of the application is the **blog post system**, fully integrated with **role-based authentication** for secure access and management.

### ✍️ Create a Post
- Logged-in users with **ROLE_USER** or **ROLE_ADMIN** can create new posts.
- Each post includes:
  - **Title**
  - **Description**
  - **Content** (with formatting support)
  - **Tags**
  - **Category**
- Posts are automatically linked to the logged-in user (author).

### 🛠 Edit a Post
- Only the **original author** or users with **ROLE_ADMIN** can edit a post.
- Editable fields:
  - Title
  - Content
  - Tags
  - Category

### ❌ Delete a Post
- **Authors** can delete their own posts.
- **Admins** can delete any post in the system.

### 📖 View Posts
- Publicly accessible list of posts with **pagination**.
- View full post details on a separate page.
- Each post shows:
  - **Title**
  - **Author**
  - **Date**
  - **Tags** and **category**
  - **Content preview**
  - **“Read More”** option

### 🔍 Search & Filter
- Users can search blog posts by:
  - **Title**
  - **Content**
  - **Tags**
  - **Author**
- Filter posts by category, date, or user.

### 🔒 Role-Based Authorization

| Feature               | **ROLE_USER** | **ROLE_ADMIN** |
|-----------------------|---------------|----------------|
| View Posts            | ✅            | ✅             |
| Create Posts          | ✅            | ✅             |
| Edit Own Posts        | ✅            | ✅             |
| Delete Own Posts      | ✅            | ✅             |
| Edit Any Post         | ❌            | ✅             |
| Delete Any Post       | ❌            | ✅             |
| Manage Users          | ❌            | ✅             |

### 🔐 Authentication
Access to routes and functionalities is secured using **Spring Security** with **JWT-based** or **Session-based** authentication (depending on your implementation).

---

### How It Works:
1. **Spring Security**: Protects routes and actions based on user roles (ROLE_USER, ROLE_ADMIN).
2. **JWT Authentication**: Used for secure token-based authentication.
3. **Role Management**: Admins can manage users, create, edit, or delete posts; users have limited access based on their role.
4. **Post Management**: Users can create, edit, or delete posts based on ownership and roles.

### Technology Stack:
- **Spring Boot** for backend API
- **Thymeleaf** for templating
- **Bootstrap** for frontend design
- **JWT** for authentication
- **MySQL** for database management
