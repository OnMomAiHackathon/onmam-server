package service;

import dto.medication.MedicationResponse;
import dto.medication.RegisterMedicationRequest;
import dto.medication.RegisterMedicationResponse;
import entity.group.OnmomGroup;
import entity.medication.OnmomMedication;
import entity.user.OnmomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import repository.group.GroupRepository;
import repository.medication.MedicationRepository;
import repository.user.UserRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicationService {
    private final MedicationRepository medicationRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;


    public RegisterMedicationResponse registerMedication(RegisterMedicationRequest request) {
        Long userId = request.getUserId();
        Long groupId = request.getGroupId();

        OnmomUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 유저 아이디입니다."));

        OnmomGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 그룹 아이디입니다."));

        boolean isUserInGroup = group.getUsers().contains(user);
        if(!isUserInGroup){
            throw new IllegalArgumentException("유저가 해당 그룹에 속해있지 않습니다.");
        }

        // 총 복용량 계산
        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1; // 기간을 일 단위로 계산
        int totalDosage = (int) days * request.getFrequency(); // 총복용량 = 기간 * 하루 복용 횟수

        // 남은 약 개수는 초기에는 총 복용량과 동일
        int remainingDosage = totalDosage;

        OnmomMedication onmomMedication = OnmomMedication.builder()
                .user(user)
                .group(group)
                .medicineName(request.getMedicineName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .frequency(request.getFrequency())
                .totalDosage(totalDosage)
                .remainingDosage(remainingDosage)
                .build();

        OnmomMedication saved = medicationRepository.save(onmomMedication);

        return RegisterMedicationResponse.builder()
                .medicationId(saved.getMedicationId())
                .message("복약 정보가 성공적으로 등록되었습니다.")
                .build();
    }

    public List<MedicationResponse> getAllMedications(Long userId, Long groupId) {
        OnmomUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 유저 아이디입니다."));

        OnmomGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 그룹 아이디입니다."));

        boolean isUserInGroup = group.getUsers().contains(user);
        if (!isUserInGroup) {
            throw new IllegalArgumentException("유저가 해당 그룹에 속해있지 않습니다.");
        }

        List<OnmomMedication> medications = medicationRepository.findByGroup(group);

        return medications.stream()
                .map(medication -> MedicationResponse.builder()
                        .medicationId(medication.getMedicationId())
                        .medicineName(medication.getMedicineName())
                        .startDate(medication.getStartDate())
                        .endDate(medication.getEndDate())
                        .frequency(medication.getFrequency())
                        .totalDosage(medication.getTotalDosage())
                        .remainingDosage(medication.getRemainingDosage())
                        .build())
                .collect(Collectors.toList());
    }


    public void updateTodayMedication(Long groupId, Long userId) {
        OnmomGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 그룹 아이디입니다."));
        OnmomUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 유저 아이디입니다."));

        LocalDate today = LocalDate.now();

        List<OnmomMedication> medications = medicationRepository.findByGroupAndUserAndStartDateLessThanEqualAndEndDateGreaterThanEqual(group, user, today, today);

        for (OnmomMedication medication : medications) {
            if (medication.getRemainingDosage() > 0) {
                int dosageToReduce = Math.min(medication.getRemainingDosage(), medication.getFrequency());
                medication.setRemainingDosage(medication.getRemainingDosage() - dosageToReduce);
                medicationRepository.save(medication);
            }
        }
    }

}
