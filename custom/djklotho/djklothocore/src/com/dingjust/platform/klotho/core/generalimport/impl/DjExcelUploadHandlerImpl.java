package com.dingjust.platform.klotho.core.generalimport.impl;

import com.dingjust.platform.klotho.core.generalimport.DjExcelUploadHandler;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.FileBasedImpExResource;
import de.hybris.platform.util.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DjExcelUploadHandlerImpl implements DjExcelUploadHandler {
    private static final String IMPORT_TEMPLATE_PATH_PREFIX = "import.template.path.";
    private static final String SAVE_TEMP_IMPEX_PATH = "save.temp.impex.path";
    private static final String MEDIA_TYPE_TXT_SUFFIX = ".txt";
    private static final String INSERT = "INSERT";
    private static final String UPDATE = "UPDATE";

    private ImportService importService;

    @Override
    public ImportResult importByImpex(InputStream inputStream, String typeCode) throws IOException {
        Path tempImpexPath = this.createImpex(inputStream, typeCode);
        return this.importImpex(tempImpexPath);
    }

    private Path createImpex(InputStream inputStream, String typeCode) throws IOException {
        final Path impexTemplateFile = Paths
                .get(new ClassPathResource(Config.getParameter(IMPORT_TEMPLATE_PATH_PREFIX + typeCode)).getURI());
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(impexTemplateFile.toFile())));

        final File impexDirectory = Paths.get(Config.getParameter(SAVE_TEMP_IMPEX_PATH)).toFile();
        if (impexDirectory.exists()) {
            if (!impexDirectory.isDirectory()) {
                impexDirectory.mkdir();
            }
        } else {
            impexDirectory.mkdir();
        }
        final Path tempImpexPath = Paths.get(Config.getParameter(SAVE_TEMP_IMPEX_PATH) + generateTempImpexName(typeCode));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempImpexPath.toFile())));

        final XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(OPCPackage.open(inputStream));

            StringBuilder impexVariables = new StringBuilder();
            String impexLine = br.readLine();
            Set<String> resolvedType = new HashSet<>();
            while (impexLine != null) {
                bw.write(impexLine + "\n");
                if(impexLine.contains(INSERT)
                        || impexLine.contains(UPDATE)) {
                    String[] impexPieces = StringUtils.split(impexLine, ";");
                    String insertType = StringUtils
                            .substringAfter(StringUtils.substringBefore(impexPieces[0], ";"), " ");
                    if (resolvedType.contains(insertType)) {
                        insertType = insertType + "|";
                        resolvedType.add(insertType);
                    } else {
                        resolvedType.add(insertType);
                    }
                    resolvedType.add(insertType);
                    Sheet currentSheet = workbook.getSheet(insertType);
                    Iterator<Row> rowIt = currentSheet.rowIterator();
                    if (rowIt.hasNext()) {
                        rowIt.next();
                    }
                    while (rowIt.hasNext()) {
                        Row row = rowIt.next();
                        StringBuilder insertData = new StringBuilder();
                        int cellNum = 0;
                        for (String impexPiece : impexPieces) {
                            if (!impexPiece.contains(INSERT)
                                    && !impexPiece.contains(UPDATE)) {
                                if (impexPiece.startsWith("$")) {
                                    insertData.append(";");
                                } else {
                                    Cell cell = row.getCell(cellNum);
                                    insertData.append(";").append(getCellValue(cell));
                                    cellNum++;
                                }
                            }
                        }
                        insertData.append("\n");
                        bw.write(insertData.toString());
                        bw.flush();
                    }
                } else {
                    impexVariables.append(impexLine);
                }
                impexLine = br.readLine();
            }
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } finally {
            br.close();
            bw.close();
        }
        return tempImpexPath;
    }

    private String getCellValue(Cell cell) {
        String cellValue = "";
        if(cell == null){
            return cellValue;
        }
        //判断数据的类型
        switch (cell.getCellType()){
            case Cell.CELL_TYPE_NUMERIC:
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING:
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                cellValue = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BLANK:
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR:
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }

    private ImportResult importImpex(Path tempImpexPath) throws IOException {
        File impexFile = tempImpexPath.toFile();
        if (impexFile != null) {
            FileBasedImpExResource resource = new FileBasedImpExResource(impexFile, "UTF-8");
            ImportResult importResult = importService.importData(resource);
            return importResult;
        } else {
            throw new FileNotFoundException();
        }
    }

    private String generateTempImpexName(String typeCode) {
        return typeCode + "_impex_" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + MEDIA_TYPE_TXT_SUFFIX;
    }

    public void setImportService(ImportService importService) {
        this.importService = importService;
    }
}
