package com.not4j.warehouse

import com.not4j.warehouse.entity.Item
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

class ItemController {

    static allowedMethods = [save: "POST", delete: "POST"]

    def itemService

    @Secured(['ROLE_ADMIN', 'ROLE_USER'])
    def index() {
        render view: 'index', model: [items: itemService.search(params.query), searchTerm: params.query]
    }

    @Secured(['ROLE_ADMIN'])
    def create() {
        render view: 'create'
    }

    @Secured(['ROLE_ADMIN', 'ROLE_USER'])
    def runningOut() {
        render view: 'runningout', model: [items: itemService.runningOut()]
    }

    @Secured(['ROLE_ADMIN'])
    def importFromFile() {
        try {
            def log = itemService.importFromFile(request.getFile('fileToUpload'))
            flash.message = log
        } catch (Exception ex) {
            println ex.printStackTrace()
            flash.message = "Can not import: ${ex.getMessage()}"
        }

        request.withFormat {
            form multipartForm {
                redirect action: "index", method: "GET"
            }
            '*' { redirect action: "index", method: "GET", [status: CREATED] }
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_USER'])
    def exportToXls() {

        try {
            response.status = 200
            response.contentType = "application/vnd.ms-excel;charset=UTF-8";
            response.setHeader "Content-disposition", "attachment; filename=export.xls"
            def outs = response.outputStream
            def exportStream = itemService.exportToXls(params.query)
            exportStream.writeTo(outs)

            outs.flush()
            outs.close()
        } catch(all) {
            flash.message = "Cannot export data ${all.getMessage()}"
            redirect action: "index", method: "GET"
        }
    }

    @Secured(['ROLE_ADMIN'])
    @Transactional
    def save(Item item) {

        if (item == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (item.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond item.errors, view:'create'
            return
        }

        item.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = "New item created with ID = $item.id"
                redirect action: "create", method: "GET"
            }
            '*' { respond item, [status: CREATED] }
        }
    }

    @Secured(['ROLE_ADMIN'])
    @Transactional
    def delete(Item item) {

        if (item == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        item.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = "Item with ID = $item.id was deleted"
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = "Item with id $params.id not found"
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
