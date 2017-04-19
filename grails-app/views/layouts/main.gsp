<!doctype html>
<html lang="en" class="no-js">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>
        <g:layoutTitle default="Grails"/>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>

    <asset:stylesheet src="application.css"/>

    <g:layoutHead/>
</head>
<body>

    <div class="navbar navbar-default navbar-static-top" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <a class="navbar-brand" href="/item">
                    Warehouse
                </a>
            </div>
            <div class="navbar-collapse collapse" aria-expanded="false" style="height: 0.8px;">
                <ul class="nav navbar-nav navbar-left">
                    <sec:ifLoggedIn>
                        <sec:ifAllGranted roles='ROLE_ADMIN'>
                            <p class="navbar-text pull-left">
                                <a href="/item/create" class="navbar-link">Create item</a>
                            </p>
                        </sec:ifAllGranted>

                        <p class="navbar-text pull-left">
                            <a href="/item/runningout" class="navbar-link">Running out</a>
                        </p>
                    </sec:ifLoggedIn>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <sec:ifLoggedIn>
                        <p class="navbar-text pull-right">
                            <g:link controller="logout" class="navbar-link">Sign out</g:link>
                        </p>
                    </sec:ifLoggedIn>
                </ul>
            </div>
        </div>
    </div>

    <g:layoutBody/>

    <div class="footer" role="contentinfo"></div>

    <div id="spinner" class="spinner" style="display:none;">
        <g:message code="spinner.alt" default="Loading&hellip;"/>
    </div>

    <asset:javascript src="application.js"/>

</body>
</html>
