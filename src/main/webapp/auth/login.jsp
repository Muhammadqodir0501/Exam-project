<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Admin Login</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
  <div class="row justify-content-center">
    <div class="col-md-6">
      <div class="card shadow-lg p-4">
        <h2 class="text-center mb-4">Admin Login</h2>
        <% if (request.getParameter("error") != null) { %>
        <div class="alert alert-danger">
          <%
            String error = request.getParameter("error");
            if ("invalid_credentials".equals(error)) {
              System.out.println("Invalid email or password.");
            } else if ("user_not_found".equals(error)) {
              System.out.println("User not found.");
            } else if ("sql_error".equals(error)) {
              System.out.println("Database error. Please try again later.");
            } else {
              System.out.println("An error occurred: " + error);
            }
          %>
        </div>
        <% } %>
        <form action="/login" method="post">
          <input type="hidden" name="loginType" value="admin">
          <div class="mb-3">
            <label for="email" class="form-label">Email</label>
            <input type="email" class="form-control" id="email" name="email" required>
          </div>
          <div class="mb-3">
            <label for="password" class="form-label">Password</label>
            <input type="password" class="form-control" id="password" name="password" required>
          </div>
          <button type="submit" class="btn btn-primary w-100">Login as Admin</button>
        </form>
        <p class="text-center mt-3">
          Want to login as User? <a href="/auth/user_login.jsp">Login as User</a>
        </p>
        <p class="text-center mt-3">
          Don't have an account? <a href="/auth/register.jsp">Register here</a>
        </p>
      </div>
    </div>
  </div>
</div>
</body>
</html>