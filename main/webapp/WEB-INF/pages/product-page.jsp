<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BookNest</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product-page.css">
</head>

<body>
	<jsp:include page="header.jsp" />
	
    <div class="container1">
        <div class="product">
            <div class="gallery">
                <img src="${pageContext.request.contextPath}/resources/images/system/book-cover-1.jpg" alt="">
            </div>
            <div class="details">
                <h1>Changing face of Microsoft</h1>
                <p>Lorem ipsum dolor, sit amet consectetur adipisicing elit. Quam, expedita nostrum similique cumque
                    ducimus impedit.dsfsdafsdfsdf asdf lorem50
                </p>
                <h2>Rs. 1500</h2>
                <button>Add to Cart</button>
            </div>
        </div>
    </div>
    <jsp:include page="footer.jsp" />
</body>

</html>