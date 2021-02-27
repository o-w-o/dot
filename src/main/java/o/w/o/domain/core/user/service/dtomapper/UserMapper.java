package o.w.o.domain.core.user.service.dtomapper;

import o.w.o.domain.core.user.domain.User;
import o.w.o.domain.core.user.service.dto.UserProfile;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {
  @Mapping(target = "id")
  UserProfile userToUserProfile(User u);

  @InheritInverseConfiguration
  User userProfileToUser(UserProfile profile);
}
