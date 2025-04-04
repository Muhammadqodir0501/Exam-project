<%@ page import="uz.pdp.demo9.config.entity.Publication" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Publication</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card shadow-lg p-4">
                <h2 class="text-center mb-4">Create Publication</h2>
                <% if (request.getParameter("error") != null) { %>
                <div class="alert alert-danger"><%= request.getParameter("error") %></div>
                <% } %>
                <form enctype="multipart/form-data" action="/publication" method="post">
                    <div class="mb-3">
                        <label>
                            <img class="rounded-circle" width="80" src="https://via.placeholder.com/80" alt="Publication Photo" id="previewImage">
                            <input name="photo" type="file" class="d-none" id="photoInput" required>
                        </label>
                    </div>
                    <div class="mb-3">
                        <label for="title" class="form-label">Title</label>
                        <input type="text" class="form-control" id="title" name="title" required>
                    </div>
                    <div class="mb-3">
                        <label for="description" class="form-label">Description</label>
                        <textarea class="form-control" id="description" name="description" rows="3" required></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Publish</button>
                    <a href="/cabinet.jsp" class="btn btn-secondary w-100 mt-2">Cancel</a>
                </form>
            </div>
        </div>
    </div>

    <div class="row mt-5">
        <h3 class="text-center">Publications</h3>
        <%
            List<Publication> publications = (List<Publication>) request.getAttribute("publications");
            if (publications != null && !publications.isEmpty()) {
                for (Publication publication : publications) {
        %>
        <div class="col-md-4 mb-3">
            <div class="card">
                <img src="/publication/<%= publication.getPublicationPhotoId() %>" class="card-img-top" alt="Publication Photo">
                <div class="card-body">
                    <h5 class="card-title"><%= publication.getTitle() %></h5>
                    <p class="card-text"><%= publication.getDescription() %></p>
                </div>
            </div>
        </div>
        <%
            }
        } else {
        %>
        <p class="text-center">No publications found.</p>
        <%
            }
        %>
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
            reader.readAsDataURL(file);
        }
    });
</script>
</body>
</html>