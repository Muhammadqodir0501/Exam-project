<%@ page import="uz.pdp.demo9.config.entity.User" %>
<%@ page import="uz.pdp.demo9.config.entity.Publication" %>
<%@ page import="uz.pdp.demo9.config.entity.Comment" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Cabinet</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<%
    User currentUser = (User) session.getAttribute("currentUser");
    System.out.println("<!-- Debug: Current user in session = " + (currentUser != null ? currentUser.getEmail() : "null") + " -->");
    System.out.println("<!-- Debug: User photoId = " + (currentUser != null ? currentUser.getPhotoId() : "null") + " -->");
%>

<div class="row bg-dark text-white p-2">
    <div class="col-2 offset-10 d-flex align-items-center">
        <img src="/publication/<%=currentUser.getPhotoId()%>" alt="User Photo" class="rounded-circle me-2" width="40" height="40" onerror="this.src='https://via.placeholder.com/40'; console.log('User photo failed to load for ID: <%=currentUser.getPhotoId()%>')">
        <span><%=currentUser.getFullName()%></span>
    </div>
</div>

<div class="container">
    <h1>Cabinet</h1>
    <h3>User Details</h3>
    <p><strong>Full Name:</strong> <%=currentUser.getFullName()%></p>
    <p><strong>Email:</strong> <%=currentUser.getEmail()%></p>
    <a href="/publication.jsp" class="btn btn-primary mb-3">Create New Publication</a>

    <h3 class="mt-4">Your Publications</h3>
    <div class="row">
        <%
            List<Publication> publications = (List<Publication>) request.getAttribute("publications");
            System.out.println("<!-- Debug: Number of publications = " + (publications != null ? publications.size() : "null") + " -->");
            if (publications != null && !publications.isEmpty()) {
                for (Publication publication : publications) {
        %>
        <div class="col-md-4 mb-3">
            <div class="card">
                <img src="/publication/<%= publication.getPublicationPhotoId() %>" class="card-img-top" alt="Publication Photo" onerror="this.src='https://via.placeholder.com/150'; console.log('Publication photo failed to load for ID: <%=publication.getPublicationPhotoId()%>')">
                <div class="card-body">
                    <h5 class="card-title"><%= publication.getTitle() %></h5>
                    <p class="card-text"><%= publication.getDescription() %></p>
                    <button class="btn btn-link" type="button" data-bs-toggle="collapse" data-bs-target="#comments-<%= publication.getId() %>" aria-expanded="false" aria-controls="comments-<%= publication.getId() %>">
                        View Comments (<%= publication.getComments() != null ? publication.getComments().size() : 0 %>)
                    </button>
                    <div class="collapse mt-2" id="comments-<%= publication.getId() %>">
                        <div class="card card-body">
                            <%
                                List<Comment> comments = publication.getComments();
                                if (comments != null && !comments.isEmpty()) {
                                    for (Comment comment : comments) {
                            %>
                            <div class="mb-2">
                                <p><strong><%= comment.getUserName() %>:</strong> <%= comment.getContent() %></p>
                                <small class="text-muted"><%= comment.getCreatedAt() %></small>
                            </div>
                            <%
                                }
                            } else {
                            %>
                            <p>No comments yet.</p>
                            <%
                                }
                            %>
                        </div>
                    </div>
                    <form action="/comment" method="post" class="mt-2">
                        <input type="hidden" name="publicationId" value="<%= publication.getId() %>">
                        <div class="input-group">
                            <input type="text" class="form-control" name="content" placeholder="Add a comment..." required>
                            <button type="submit" class="btn btn-primary">Comment</button>
                        </div>
                    </form>
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

    <form action="/logout" method="post">
        <button type="submit" class="btn btn-danger">Logout</button>
    </form>
</div>
</body>
</html>