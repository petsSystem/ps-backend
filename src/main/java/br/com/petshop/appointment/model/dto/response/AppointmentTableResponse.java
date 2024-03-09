package br.com.petshop.appointment.model.dto.response;

import br.com.petshop.appointment.model.enums.PaymentStatus;
import br.com.petshop.appointment.model.enums.PaymentType;
import br.com.petshop.appointment.model.enums.Status;
import br.com.petshop.category.model.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Classe dto responsável pelo retorno dos dados de agendamento resumidos para listagem.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentTableResponse implements Serializable {
    private UUID id;
    private Category category;
//    private UUID companyId;
//    private UUID petId;
    private String petName;
//    private UUID customerId;
    private String customerName;
    private String phone;
    private UUID scheduleId;
//    private UUID userId;
    private String userName;
//    private UUID productId;
    private String productName;
//    private List<UUID> additionalIds;
    private List<String> additionals;
    private String date;
    private String time;
    private PaymentStatus paymentStatus;
    private PaymentType paymentType;
    private Status status;
    private String comments;
    private BigDecimal amount;
    private Boolean active;
}
