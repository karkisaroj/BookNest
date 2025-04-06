<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>BookNest</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css" />
</head>

<body>
  <div class="container">
    <div class="left-side">
        <h1 class="tagline">Nest Your Book and Be in Peace</h1>
        <img class="book-stack" src="${pageContext.request.contextPath}/resources/images/system/bookstack.png" alt="Stack of books">
    </div>
    <div class="right-side">
        <div class="registration-container">
            <h1 class="registration-title">Login</h1>
            
            <form>
                
                <div class="form-group">
                    <input type="text" id="firstName" name="userName">
                    <label for="firstName">User Name</label>
                </div>
                    

                

                <div class="form-group">
                    <input type="password" id="password" name="password">
                    <label for="password">Password</label>
                </div>
                
                <button type="submit" class="btn-create">Login</button>
                <div class="bottom-links">
                    <a href="${pageContext.request.contextPath}/register">Don't have an account? <span>Register</span> </a>
                    
                </div>

            </form>
        </div>
    </div>
</div>
</body>

</html>