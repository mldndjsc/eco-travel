package com.ecotravel.vodici.service;

import com.ecotravel.vodici.dto.request.CertificationRequestDTO;
import com.ecotravel.vodici.dto.request.GuideRequestDTO;
import com.ecotravel.vodici.dto.response.CertificationResponseDTO;
import com.ecotravel.vodici.dto.response.GuideResponseDTO;
import com.ecotravel.vodici.dto.response.LanguageResponseDTO;
import com.ecotravel.vodici.entity.*;
import com.ecotravel.vodici.exception.BusinessException;
import com.ecotravel.vodici.exception.ResourceNotFoundException;
import com.ecotravel.vodici.messaging.publisher.GuideEventPublisher;
import com.ecotravel.vodici.repository.CertificationRepository;
import com.ecotravel.vodici.repository.GuideRepository;
import lombok.RequiredArgsConstructor;
import org.raflab.shared.events.GuideCreatedEvent;
import org.raflab.shared.events.GuideDeletedEvent;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@RefreshScope
public class GuideService {

    private final GuideRepository guideRepository;
    private final CertificationRepository certificationRepository;
    private final LanguageService languageService;
    private final GuideEventPublisher eventPublisher;

    @Transactional
    public GuideResponseDTO create(GuideRequestDTO dto) {
        Guide guide = new Guide();
        guide.setFirstName(dto.getFirstName());
        guide.setLastName(dto.getLastName());
        guide.setBio(dto.getBio());
        guide.setRating(dto.getRating());
        GuideResponseDTO response = toDTO(guideRepository.save(guide));
        eventPublisher.publishGuideCreated(new GuideCreatedEvent(response.getId(), response.getFirstName(), response.getLastName()));
        return response;
    }

    @Transactional(readOnly = true)
    public List<GuideResponseDTO> findAll(Double minRating, Long languageId) {
        return guideRepository.findWithFilters(minRating, languageId)
                .stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public GuideResponseDTO findById(Long id) {
        return toDTO(getOrThrow(id));
    }

    @Transactional
    public GuideResponseDTO update(Long id, GuideRequestDTO dto) {
        Guide guide = getOrThrow(id);
        guide.setFirstName(dto.getFirstName());
        guide.setLastName(dto.getLastName());
        guide.setBio(dto.getBio());
        guide.setRating(dto.getRating());
        return toDTO(guideRepository.save(guide));
    }

    @Transactional
    public void delete(Long id) {
        Guide guide = getOrThrow(id);
        String firstName = guide.getFirstName();
        String lastName = guide.getLastName();
        guideRepository.deleteById(id);
        eventPublisher.publishGuideDeleted(new GuideDeletedEvent(id, firstName, lastName));
    }

    @Transactional
    public GuideResponseDTO assignLanguage(Long guideId, Long languageId) {
        Guide guide = getOrThrow(guideId);
        Language language = languageService.getOrThrow(languageId);

        boolean alreadyAssigned = guide.getGuideLanguages().stream()
                .anyMatch(gl -> gl.getLanguage().getId().equals(languageId));
        if (alreadyAssigned) {
            throw new BusinessException("Vodic vec govori ovaj jezik");
        }

        GuideLanguage guideLanguage = new GuideLanguage();
        guideLanguage.setGuide(guide);
        guideLanguage.setLanguage(language);
        guide.getGuideLanguages().add(guideLanguage);
        return toDTO(guideRepository.save(guide));
    }

    @Transactional
    public GuideResponseDTO removeLanguage(Long guideId, Long languageId) {
        Guide guide = getOrThrow(guideId);
        guide.getGuideLanguages().removeIf(gl -> gl.getLanguage().getId().equals(languageId));
        return toDTO(guideRepository.save(guide));
    }

    @Transactional
    public CertificationResponseDTO addCertification(Long guideId, CertificationRequestDTO dto) {
        Guide guide = getOrThrow(guideId);
        Certification cert = new Certification();
        cert.setGuide(guide);
        cert.setName(dto.getName());
        cert.setIssuingBody(dto.getIssuingBody());
        cert.setExpiryDate(dto.getExpiryDate());
        return toCertDTO(certificationRepository.save(cert));
    }

    @Transactional(readOnly = true)
    public List<CertificationResponseDTO> getCertifications(Long guideId, boolean validOnly) {
        getOrThrow(guideId);
        List<Certification> certs = validOnly
                ? certificationRepository.findValidByGuideId(guideId, LocalDate.now())
                : certificationRepository.findByGuideId(guideId);
        return certs.stream().map(this::toCertDTO).toList();
    }

    private Guide getOrThrow(Long id) {
        return guideRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vodic sa ID " + id + " nije pronadjen"));
    }

    GuideResponseDTO toDTO(Guide guide) {
        int validCertCount = (int) certificationRepository.countValidByGuideId(guide.getId(), LocalDate.now());

        List<LanguageResponseDTO> languages = guide.getGuideLanguages().stream()
                .map(gl -> languageService.toDTO(gl.getLanguage()))
                .toList();

        return new GuideResponseDTO(
                guide.getId(),
                guide.getFirstName(),
                guide.getLastName(),
                guide.getBio(),
                guide.getRating(),
                languages,
                validCertCount
        );
    }

    private CertificationResponseDTO toCertDTO(Certification cert) {
        return new CertificationResponseDTO(
                cert.getId(),
                cert.getName(),
                cert.getIssuingBody(),
                cert.getExpiryDate(),
                cert.getExpiryDate() != null && cert.getExpiryDate().isAfter(LocalDate.now())
        );
    }
}
