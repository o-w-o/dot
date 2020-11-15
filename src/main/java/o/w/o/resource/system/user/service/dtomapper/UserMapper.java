package o.w.o.resource.system.user.service.dtomapper;

import o.w.o.resource.system.user.domain.User;
import o.w.o.resource.system.user.service.dto.UserProfile;
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
