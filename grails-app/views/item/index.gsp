<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>My Warehouse</title>

    <asset:link rel="icon" href="favicon.ico" type="image/x-ico" />
</head>
<body>
    <!-- modal dialog for uploading csv file -->
    <div class="modal fade" id="uploadDialog">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"
                            aria-hidden="true">&times;</button>
                    <h4 class="modal-title">Import items from CSV/XLS file</h4>
                </div>
                <div class="modal-body">
                    <g:uploadForm action="import">
                        <div class="form-group">
                            <p>Select XLS/CSV file with 3 columns: <b>'ExternalId', 'Name', 'Amount'</b></p>
                            <label class="sr-only" for="fileToUpload">Choose file to import items from</label>
                            <label class="btn btn-primary btn-file" id="fileToUpload">
                                Browse <input type="file" name="fileToUpload" style="display: none;">
                            </label>
                        </div>
                        <input class="btn btn-default" type="submit" value="Import"/>
                    </g:uploadForm>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
    <!-- /.modal -->

    <div id="content" role="main">
        <section class="row colset-2-its">
            <div class="container">
                <div class="container">
                    <g:if test="${flash.message}">
                        <div class="alert alert-info">
                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                            ${flash.message}
                        </div>
                    </g:if>
                </div>
            </div>

            <div class="container">
                <form action="/item" method="get" class="form-inline" role="form">
                    <div class="form-group">
                        <label class="sr-only" for="query">label</label>
                        <input type="text" class="form-control" name="query" id="query" placeholder="Search for...">
                    </div>
                    <button type="submit" class="btn btn-primary">Search</button>
                </form>
                <h1>List of items</h1>
                <div class="btn-group">
                    <sec:ifAllGranted roles='ROLE_ADMIN'>
                        <a class="btn btn-default" data-toggle="modal" href="#uploadDialog">Import from CSV/XLS</a>
                    </sec:ifAllGranted>
                    <a class="btn btn-default" href="/item/exportToXls?query=${searchTerm}">Export to XLS</a>
                </div>
                <table class="table table-striped table-hover">
                    <thead>
                    <tr>
                        <th>ExtId</th>
                        <th>Name</th>
                        <th>Brand</th>
                        <th>Price</th>
                        <th>Size</th>
                        <th>Amount</th>
                        <th></th>
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
                            <td>
                                <sec:ifAllGranted roles='ROLE_ADMIN'>
                                    <g:form id="${c.id}">
                                        <g:actionSubmit class="btn btn-danger" action="delete" id="${c.id}" value="Delete"/>
                                    </g:form>
                                </sec:ifAllGranted>
                            </td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
        </section>
    </div>

</body>
</html>
