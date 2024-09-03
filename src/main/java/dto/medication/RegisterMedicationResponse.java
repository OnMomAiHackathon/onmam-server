package dto.medication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RegisterMedicationResponse {
    private Long medicationId;
    private String message;
}
