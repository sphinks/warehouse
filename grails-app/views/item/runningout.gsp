<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>My Warehouse</title>

    <asset:link rel="icon" href="favicon.ico" type="image/x-ico" />
</head>
<body>
<div id="content" role="main">
    <section class="row colset-2-its">
        <div class="container">
            <h1>Items running out of stock</h1>

            <table class="table table-striped table-hover">
                <thead>
                <tr>
                    <th>ExternalId</th>
                    <th>Name</th>
                    <th>Brand</th>
                    <th>Price</th>
                    <th>Size</th>
                    <th>Amount</th>
                </tr>
                </thead>
                <tbody>
                <g:each var="c" in="${items}">
                    <tr>
                        <td>${c.externalId}</td>
                        <td>${c.name}</td>
                        <td>${c.brand}</td>
                        <td>${c.price} rub.</td>
                        <td>${c.size} ml</td>
                        <td>${c.amount}</td>
                    </tr>
                </g:each>
                </tbody>
            </table>
        </div>
    </section>
</div>

</body>
</html>
