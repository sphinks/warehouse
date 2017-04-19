import com.not4j.warehouse.entity.Item
import com.not4j.warehouse.exception.ExportException
import com.not4j.warehouse.exception.ImportException
import grails.transaction.Transactional
import jxl.Sheet
import jxl.Workbook
import jxl.write.Label
import jxl.write.Number
import jxl.write.WritableSheet
import jxl.write.WritableWorkbook
import org.springframework.web.multipart.MultipartFile

@Transactional
class ItemService {

    private static int AMOUNT_THRESHOLD = 5
    private static String CSV_TYPE = 'text/csv'
    private static String XLS_TYPE = 'application/vnd.ms-excel'

    def search(String query) {
        def itemsList
        if (!query || query.isEmpty()) {
            itemsList = Item.list()
        }else{
            itemsList = Item.findAllByNameOrBrand(query, query)
        }

        return itemsList
    }

    def runningOut() {
        return Item.findAllByAmountLessThan(AMOUNT_THRESHOLD)
    }

    def importFromFile(MultipartFile file) {

        log.debug("File type is ${file.contentType}")

        if (file.contentType == CSV_TYPE) {
            return importFromCsvFile(file.inputStream)
        }else if (file.contentType == XLS_TYPE) {
            return importFromXlsFile(file.inputStream)
        }else{
            return "File type is unknown, cannot import"
        }
    }

    def exportToXls(String query) {

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        List<Item> items = search(query)

        WritableWorkbook workbook = null
        try {

            workbook = Workbook.createWorkbook(os)

            WritableSheet excelSheet = workbook.createSheet("Sheet 1", 0)

            excelSheet.addCell(new Label(0, 0, "External ID"))
            excelSheet.addCell(new Label(1, 0, "Name"))
            excelSheet.addCell(new Label(2, 0, "Brand"))
            excelSheet.addCell(new Label(3, 0, "Price"))
            excelSheet.addCell(new Label(4, 0, "Size"))
            excelSheet.addCell(new Label(5, 0, "Amount"))

            items.eachWithIndex{ it, i ->
                i++
                excelSheet.addCell(new Label(0, i, it.externalId))
                excelSheet.addCell(new Label(1, i, it.name))
                excelSheet.addCell(new Label(2, i, it.brand))
                excelSheet.addCell(new Number(3, i, it.price))
                excelSheet.addCell(new Number(4, i, it.size))
                excelSheet.addCell(new Number(5, i, it.amount))
            }
            workbook.write()
        } catch (all) {
            log.debug("Cannot export data: ${all.getMessage()}")
            throw new ExportException(all.toString())
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (all) {
                    log.debug("Cannot close workbook: ${all.getMessage()}")
                    throw new ExportException(all.toString())
                }
            }
        }
        return os
    }

    private String importFromCsvFile(InputStream is) {
        StringBuilder eventLog = new StringBuilder()

        is.splitEachLine(',') { fields ->
            try {
                def rowToParse = fields.collect{it.trim()} as List<String>
                def item = parseItem(rowToParse)
                eventLog.append(updateItem(item))
            } catch(all) {
                eventLog.append("Cannot parse item ${all.getMessage()}\n")
            }
        }
        return eventLog
    }

    private String importFromXlsFile(InputStream is) {

        Workbook workbook = null;
        StringBuilder eventLog = new StringBuilder()

        try {
            workbook = Workbook.getWorkbook(is)
            Sheet sheet = workbook.getSheet(0)

            def rowsCount = sheet.getRows()

            for (int i = 1; i < rowsCount; i++) {
                try {
                    def rowToParse = sheet.getRow(i).collect { it.getContents().trim() } as List<String>
                    def item = parseItem(rowToParse)
                    eventLog.append(updateItem(item))
                } catch(all) {
                    eventLog.append("Cannot parse item: ${all.getMessage()}\n")
                }
            }
        } catch (all) {
            log.debug("Cannot import data: ${all.getMessage()}")
            eventLog.append("Cannot import data: ${all.getMessage()}\n")
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (all) {
                    log.debug("Cannot close workbook: ${all.getMessage()}")
                    eventLog.append("Cannot close workbook: ${all.getMessage()}\n")
                }
            }
        }
        return eventLog.toString()
    }

    private Item parseItem(List<String> rowsToParse) {

        def item = null

        try {
            item = new Item(
                    externalId: rowsToParse.get(0),
                    name: rowsToParse.get(1),
                    amount: Integer.parseInt(rowsToParse.get(2)))
        } catch(all) {
            log.debug("Cannot parse item with rows: $rowsToParse, $all")
            throw new ImportException("Cannot parse item with rows: $rowsToParse")
        }
        return item
    }

    private String updateItem(Item item) {

        StringBuilder eventLog = new StringBuilder()
        def itemToUpdate = Item.findByExternalId(item.externalId)

        if (!itemToUpdate) {
            eventLog.append("There is no object with ID '${item.externalId}' in DB - not updated\n")
        }else{
            itemToUpdate.amount += item.amount
            if (itemToUpdate.hasErrors() || itemToUpdate.save(flush: true) == null) {
                eventLog.append("Could not update item: ${item.externalId}, ${item.errors}\n")
            }
        }

        return eventLog.toString()
    }

}
