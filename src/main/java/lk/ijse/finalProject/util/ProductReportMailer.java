package lk.ijse.finalProject.util;

import lk.ijse.finalProject.db.DBConnection;
import net.sf.jasperreports.engine.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ProductReportMailer {

    public static boolean sendLastProductionReport() {
        try {
            // 1. Get last production id
            String lastProductionId = getLastProductionId();
            if (lastProductionId == null) return false;

            // 2. Prepare Jasper report
            String jrxmlPath = "/report/productPlacementReport.jrxml";
            JasperReport report = JasperCompileManager.compileReport(
                    ProductReportMailer.class.getResourceAsStream(jrxmlPath)
            );
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("productionId", lastProductionId);

            Connection connection = DBConnection.getInstance().getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, connection);

            // 3. Export to PDF
            String outputFilePath = "production_report_" + lastProductionId + "_" + LocalDate.now() + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputFilePath);

            // 4. Send email with attachment
            return EmailUtil.sendMailWithAttachment(
                    "shalukaofficial24@gmail.com",
                    "Production Placement Report",
                    "Please find the attached production placement report for Production ID: " + lastProductionId,
                    outputFilePath
            );
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String getLastProductionId() throws Exception {
        String sql = "SELECT production_id FROM production ORDER BY production_id DESC LIMIT 1";
        try (
                ResultSet rs = CrudUtil.execute(sql)
        ) {
            if (rs.next()) {
                return rs.getString("production_id");
            }
            return null;
        }
    }
}