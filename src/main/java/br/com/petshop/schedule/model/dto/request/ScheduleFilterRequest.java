package br.com.petshop.schedule.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleFilterRequest implements Serializable {
    private UUID companyId;
    private UUID categoryId;
    private UUID userId;
    private UUID productId;
}
