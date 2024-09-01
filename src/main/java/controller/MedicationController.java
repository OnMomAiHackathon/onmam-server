package controller;

import dto.medication.RegisterMedicationRequest;
import dto.medication.RegisterMedicationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.MedicationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("medication")
public class MedicationController {
    private final MedicationService medicationService;


    @PostMapping
    public ResponseEntity<RegisterMedicationResponse> registerMedication(@RequestBody RegisterMedicationRequest registerMedicationRequest){
        RegisterMedicationResponse response = medicationService.registerMedication(registerMedicationRequest);
        return ResponseEntity.ok(response);
    }

}
