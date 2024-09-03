package controller;

import dto.medication.MedicationResponse;
import dto.medication.RegisterMedicationRequest;
import dto.medication.RegisterMedicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.MedicationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("medication")
@Tag(name = "복약 관련 API")
public class MedicationController {
    private final MedicationService medicationService;


    @PostMapping
    @Operation(summary = "약 정보 등록", description = "자식이 부모의 약 정보를 등록합니다.")
    public ResponseEntity<RegisterMedicationResponse> registerMedication(@RequestBody RegisterMedicationRequest registerMedicationRequest){
        RegisterMedicationResponse response = medicationService.registerMedication(registerMedicationRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "모든 약 정보 불러오기", description = "그룹 내에 등록된 모든 약 정보가 가져와집니다.")
    public ResponseEntity<List<MedicationResponse>> getAllMedications(@RequestParam Long userId, @RequestParam Long groupId) {
        List<MedicationResponse> response = medicationService.getAllMedications(userId, groupId);
        return ResponseEntity.ok(response);
    }



}
