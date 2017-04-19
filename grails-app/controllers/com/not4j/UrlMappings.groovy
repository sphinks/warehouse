package com.not4j

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller:"item", method:"GET")
        "/item"(controller:"item", action: "save", method: "POST")
        "/item/import"(controller:"item", action: "importFromFile", method: "POST")
        "/item/runningout"(controller:"item", action: "runningOut", method: "GET")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
