package com.dingjust.platform.klotho.core.generalimport;

import de.hybris.platform.servicelayer.impex.ImportResult;

import java.io.IOException;
import java.io.InputStream;

public interface DjExcelUploadHandler {
    ImportResult importByImpex(InputStream inputStream, String typeCode) throws IOException;
}
