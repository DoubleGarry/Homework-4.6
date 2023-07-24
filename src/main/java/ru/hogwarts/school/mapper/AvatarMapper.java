package ru.hogwarts.school.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import ru.hogwarts.school.controller.AvatarController;
import ru.hogwarts.school.dto.AvatarDto;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.service.InfoService;

@Component
@AllArgsConstructor
public class AvatarMapper {
    private final AvatarRepository avatarRepository;
    private final InfoService infoService;

    public AvatarDto toDto(Avatar avatar) {
        return AvatarDto.builder()
                .id(avatar.getId())
                .avatarUrl(
                        UriComponentsBuilder.newInstance()
                                .scheme("http")
                                .host("localhost")
                                .port(infoService.getPort())
                                .pathSegment(AvatarController.BASE_PATH, String.valueOf(avatar.getStudent().getId()))
                                .toUriString()
                )
                .fileSize(avatar.getFileSize())
                .mediaType(avatar.getMediaType())
                .studentId(avatarRepository.studentId(avatar.getId()))
                .build();
    }

}
