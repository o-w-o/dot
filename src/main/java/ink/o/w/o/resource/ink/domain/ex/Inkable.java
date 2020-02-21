package ink.o.w.o.resource.ink.domain.ex;

import java.util.Map;

public interface Inkable<T extends InkBasic> {

  Map<String, Object> dehydrateEx(T ink);

  default InkBasic dehydrate() {
    var ink = (T) this;

    return new InkBasic()
        .setId(ink.getId())
        .setType(ink.getType())
        .setDescription(ink.getDescription())
        .setFeatures(ink.getFeatures())
        .setUnits(ink.getUnits())
        .setMarks(ink.getMarks())
        .setParticipants(ink.getParticipants())
        .setRefs(ink.getRefs())
        .setTitle(ink.getTitle())
        .setEx(dehydrateEx(ink));
  }

  default InkBasic dehydrate(T ink) {
    return new InkBasic()
        .setId(ink.getId())
        .setType(ink.getType())
        .setDescription(ink.getDescription())
        .setFeatures(ink.getFeatures())
        .setUnits(ink.getUnits())
        .setMarks(ink.getMarks())
        .setParticipants(ink.getParticipants())
        .setRefs(ink.getRefs())
        .setTitle(ink.getTitle())
        .setEx(dehydrateEx(ink));
  }

  T hydrateEx(InkBasic inkBasic);

  default T hydrate(InkBasic inkBasic) {
    var that = (T) this;

    that
        .setId(inkBasic.getId())
        .setType(inkBasic.getType())
        .setDescription(inkBasic.getDescription())
        .setFeatures(inkBasic.getFeatures())
        .setUnits(inkBasic.getUnits())
        .setMarks(inkBasic.getMarks())
        .setParticipants(inkBasic.getParticipants())
        .setRefs(inkBasic.getRefs())
        .setTitle(inkBasic.getTitle());

    return hydrateEx(inkBasic);
  }


}
