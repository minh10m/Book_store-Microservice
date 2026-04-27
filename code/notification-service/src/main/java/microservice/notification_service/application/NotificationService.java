package microservice.notification_service.application;

import jakarta.mail.internet.MimeMessage;
import microservice.notification_service.config.ApplicationProperties;
import microservice.notification_service.domain.model.OrderCancelledEvent;
import microservice.notification_service.domain.model.OrderCreatedEvent;
import microservice.notification_service.domain.model.OrderDeliveredEvent;
import microservice.notification_service.domain.model.OrderErrorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final JavaMailSender emailSender;
    private final ApplicationProperties properties;

    public NotificationService(JavaMailSender emailSender, ApplicationProperties properties) {
        this.emailSender = emailSender;
        this.properties = properties;
    }

    public void sendOrderCreatedNotification(OrderCreatedEvent event) {
        String message =
                """
                ===================================================
                Thông báo: Đặt hàng thành công
                ----------------------------------------------------
                Chào %s,
                Đơn hàng của bạn với mã số: %s đã được khởi tạo thành công.
                Chúng tôi sẽ sớm xử lý và thông báo cho bạn khi có cập nhật mới.

                Cảm ơn bạn đã tin tưởng BookStore!
                ===================================================
                """
                        .formatted(event.customer().name(), event.orderNumber());
        log.info("\n{}", message);
        sendEmail(event.customer().email(), "Xác nhận đặt hàng thành công - BookStore", message);
    }

    public void sendOrderPaidNotification(OrderDeliveredEvent event) {
        String message =
                """
                ===================================================
                Thông báo: Thanh toán thành công
                ----------------------------------------------------
                Chào %s,
                Đơn hàng mã số: %s của bạn đã được thanh toán thành công.
                Thời gian thanh toán: %s

                Đơn hàng của bạn hiện đang được chuẩn bị để giao.
                Cảm ơn bạn đã mua sắm tại BookStore!
                ===================================================
                """
                        .formatted(event.customer().name(), event.orderNumber(), event.createdAt());
        log.info("\n{}", message);
        sendEmail(event.customer().email(), "Thông báo thanh toán thành công - BookStore", message);
    }

    public void sendOrderCancelledNotification(OrderCancelledEvent event) {
        String message =
                """
                ===================================================
                Thông báo: Hủy đơn hàng
                ----------------------------------------------------
                Chào %s,
                Đơn hàng mã số: %s của bạn đã bị hủy.
                Lý do: %s

                Nếu đây là một sự nhầm lẫn, vui lòng liên hệ với bộ phận hỗ trợ của chúng tôi.
                BookStore xin lỗi vì sự bất tiện này.
                ===================================================
                """
                        .formatted(event.customer().name(), event.orderNumber(), event.reason());
        log.info("\n{}", message);
        sendEmail(event.customer().email(), "Thông báo hủy đơn hàng - BookStore", message);
    }

    public void sendOrderErrorEventNotification(OrderErrorEvent event) {
        String message =
                """
                ===================================================
                CẢNH BÁO: Lỗi xử lý đơn hàng (Hệ thống)
                ----------------------------------------------------
                Chào Quản trị viên (%s),
                Hệ thống gặp lỗi khi xử lý đơn hàng số: %s.
                Nội dung lỗi: %s

                Vui lòng kiểm tra hệ thống ngay lập tức.
                ===================================================
                """
                        .formatted(properties.supportEmail(), event.orderNumber(), event.reason());
        log.info("\n{}", message);
        sendEmail(properties.supportEmail(), "CẢNH BÁO LỖI HỆ THỐNG - BookStore", message);
    }

    private void sendEmail(String recipient, String subject, String content) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(properties.supportEmail());
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(content);
            emailSender.send(mimeMessage);
            log.info("Email sent to: {}", recipient);
        } catch (Exception e) {
            log.error("Error while sending email to {}: {}", recipient, e.getMessage());
        }
    }
}
