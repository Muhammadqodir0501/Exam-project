<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card shadow-lg p-4">
                <h2 class="text-center mb-4">Register</h2>
                <% if (request.getParameter("error") != null) { %>
                <div class="alert alert-danger"><%= request.getParameter("error") %></div>
                <% } %>
                <form action="/register" method="post" enctype="multipart/form-data">
                    <div class="mb-3">
                        <label>
                            <img class="rounded-circle" width="80" src="https://via.placeholder.com/80" alt="Profile Photo" id="previewImage">
                            <input name="photo" type="file" class="d-none" id="photoInput" accept="image/*" required>
                        </label>
                    </div>
                    <div class="mb-3">
                        <label for="firstName" class="form-label">First Name</label>
                        <input type="text" class="form-control" id="firstName" name="firstName" required>
                    </div>
                    <div class="mb-3">
                        <label for="lastName" class="form-label">Last Name</label>
                        <input type="text" class="form-control" id="lastName" name="lastName" required>
                    </div>
                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="email" class="form-control" id="email" name="email" required>
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Password</label>
                        <input type="password" class="form-control" id="password" name="password" required>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Register</button>
                </form>
                <p class="text-center mt-3">
                    Already have an account? <a href="/auth/user_login.jsp">Login here</a>
                </p>
            </div>
        </div>
    </div>
</div>

<script>
    document.getElementById('previewImage').addEventListener('click', function() {
        document.getElementById('photoInput').click();
    });

    document.getElementById('photoInput').addEventListener('change', function(event) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                document.getElementById('previewImage').src = e.target.result;
            };
            reader.onerror = function(e) {
                console.error('Error reading file:', e);
                document.getElementById('previewImage').src = 'https://via.placeholder.com/80';
            };
            reader.readAsDataURL(file);
        } else {
            console.log('No file selected');
            document.getElementById('previewImage').src = 'https://via.placeholder.com/80';
        }
    });
</script>
</body>
</html>