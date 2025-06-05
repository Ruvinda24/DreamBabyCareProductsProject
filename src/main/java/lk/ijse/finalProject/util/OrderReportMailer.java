package lk.ijse.finalProject.util;

import lk.ijse.finalProject.db.DBConnection;
import net.sf.jasperreports.engine.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class OrderReportMailer {

    public static boolean sendLastOrderReport() {
        try {
            // 1. Get last order id
            String lastOrderId = getLastOrderId();
            if (lastOrderId == null) return false;

            // 2. Prepare Jasper report
            String jrxmlPath = "/report/orderPlacementReport.jrxml";
            JasperReport report = JasperCompileManager.compileReport(
                    OrderReportMailer.class.getResourceAsStream(jrxmlPath)
            );
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("orderId", lastOrderId);

            Connection connection = DBConnection.getInstance().getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, connection);

            // 3. Export to PDF
            String outputFilePath = "order_report_" + lastOrderId + LocalDate.now() + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputFilePath);

            // 4. Send email with attachment
            return EmailUtil.sendMailWithAttachment(
                    "shalukaofficial24@gmail.com",
                    "Order Report",
                    "Please find the attached order report for Order ID: " + lastOrderId,
                    outputFilePath
            );
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String getLastOrderId() throws Exception {
        String sql = "SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1";
        try (
                ResultSet rs = CrudUtil.execute(sql)
        ) {
            if (rs.next()) {
                return rs.getString("order_id");
            }
            return null;
        }
    }
}