package com.iv1201.recruitmentapplication.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iv1201.recruitmentapplication.entity.*;
import com.iv1201.recruitmentapplication.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class DTOUtil {

    public <T> T fromMap(Object map, Class<T> tClass) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        T t = mapper.convertValue(map, tClass);
        return t;
    }

    public static <T> T mapDTO(Object map, Class<T> tClass) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        T t = mapper.convertValue(map, tClass);
        return t;
    }


    public static ApplicantDTO applicantDTO(Applicant applicant) {
        if (Objects.isNull(applicant))
            return null;
        ApplicationDTO applicationDTO = applicationDTO(applicant.getApplication());
        return ApplicantDTO.builder()
                .application(applicationDTO)
                .email(applicant.getEmail())
                .name(applicant.getName())
                .surname(applicant.getSurname())
                .pnr(applicant.getPnr())
                .username(applicant.getUsername())
                .build();
    }

    public static ApplicationDTO applicationDTO(Applicant applicant) {
        return applicationDTO(applicant.getApplication());
    }

    public static ApplicationDTO applicationDTO(Application application) {
        if (Objects.isNull(application)) {
            return null;
        } else {
            List<CompetenceDTO> competenceList = new ArrayList<>();

            for (ApplicationCompetence competence : application.getCompetences()) {
                competenceList.add(new CompetenceDTO(competence.getCompetence().getCompetence(), competence.getYearsExperience()));
            }
            List<AvailabilityDTO> availabilityDTOS = availabilityDTO(application.getAvailability());
            return ApplicationDTO.builder()
                    .version(application.getVersion())
                    .availability(availabilityDTOS)
                    .state(application.getApplicationState().getState().name())
                    .id(application.getId())

                    .competences(competenceList)
                    .build();
        }
    }

    /**
     * Convert password reset token entity to password reset token DTO.
     * @param passwordResetToken The entity to create a DTO of.
     * @return The created DTO object.
     */
    public static PasswordResetTokenDTO passwordResetTokenDTO(PasswordResetToken passwordResetToken) {
        if (Objects.isNull(passwordResetToken))
            return null;
        return PasswordResetTokenDTO.builder()
                .token(passwordResetToken.getToken())
                .applicant(applicantDTO(passwordResetToken.getApplicant()))
                .build();
    }

    public static List<AvailabilityDTO> availabilityDTO(List<Availability> availability) {

        if (Objects.isNull(availability)) {
            return null;
        } else {
            return availability
                    .stream()
                    .map(av -> AvailabilityDTO.builder()
                            .dateFrom(av.getDateFrom())
                            .dateTo(av.getDateTo())
                            .build()).collect(Collectors.toList());
        }
    }

    public static List<AvailabilityDTO> availabilityDTO(Availability... availability) {
        return availabilityDTO(List.of(availability));
    }

    public interface MappingStrategy<T> {
        void apply(ObjectMapper mapper, Consumer<T> consumer);
    }
}
