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
            <g:hasErrors>
                <div class="alert alert-danger">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                    <g:eachError><p><g:message error="${it}"/></p></g:eachError>
                </div>
            </g:hasErrors>
            <g:if test="${flash.message}">
                <div class="alert alert-danger">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                    ${flash.message}
                </div>
            </g:if>
        </div>
        <div class="container">
            <form action="/item" method="post" class="form-horizontal" role="form">
                <div class="form-group">
                    <legend>Add new Item</legend>
                </div>

                <div class="form-group">
                    <div class="col-sm-2">
                        <input type="text" class="form-control" name="externalId" value="${item?.externalId}" placeholder="Id">
                    </div>
                    <div class="col-sm-2">
                        <input type="text" class="form-control" name="name" value="${item?.name}" placeholder="Name">
                    </div>
                    <div class="col-sm-2">
                        <input type="text" class="form-control" name="brand" value="${item?.brand}" placeholder="Brand">
                    </div>
                    <div class="col-sm-2">
                        <input type="number" step="0.01" class="form-control" name="price" value="${item?.price}" placeholder="Price">
                    </div>
                    <div class="col-sm-2">
                        <input type="number" class="form-control" name="size" value="${item?.size}" placeholder="Size">
                    </div>
                    <div class="col-sm-2">
                        <input type="number" class="form-control" name="amount" value="${item?.amount}" placeholder="amount">
                    </div>
                </div>


                <div class="form-group">
                    <div class="col-sm-10">
                        <button type="submit" class="btn btn-primary">Save</button>
                    </div>
                </div>
            </form>
        </div>
    </section>
</div>

</body>
</html>
