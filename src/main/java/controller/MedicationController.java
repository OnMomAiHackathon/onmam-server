package controller;

import dto.medication.MedicationResponse;
import dto.medication.RegisterMedicationRequest;
import dto.medication.RegisterMedicationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.MedicationService;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<MedicationResponse>> getUserMedications(@RequestParam Long userId, @RequestParam Long groupId) {
        List<MedicationResponse> response = medicationService.getAllMedications(userId, groupId);
        return ResponseEntity.ok(response);
    }



}
