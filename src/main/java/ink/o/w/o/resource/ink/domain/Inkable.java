package ink.o.w.o.resource.ink.domain;

import java.util.Map;

public interface Inkable<T extends InkBasic> {

  Map<String, Object> dehydrateEx();

  default InkBasic dehydrate() {
    var ink = (T) this;

    return new InkBasic()
        .setId(ink.getId())
        .setType(ink.getType())
        .setDescription(ink.getDescription())
        .setFeatures(ink.getFeatures())
        .setDots(ink.getDots())
        .setMarks(ink.getMarks())
        .setParticipants(ink.getParticipants())
        .setRefs(ink.getRefs())
        .setTitle(ink.getTitle())
        .setSource(dehydrateEx());
  }

  default InkBasic dehydrate(T ink) {
    return new InkBasic()
        .setId(ink.getId())
        .setType(ink.getType())
        .setDescription(ink.getDescription())
        .setFeatures(ink.getFeatures())
        .setDots(ink.getDots())
        .setMarks(ink.getMarks())
        .setParticipants(ink.getParticipants())
        .setRefs(ink.getRefs())
        .setTitle(ink.getTitle())
        .setSource(dehydrateEx());
  }

  T hydrateEx(InkBasic inkBasic);

  default T hydrate(InkBasic inkBasic) {
    var that = (T) this;

    that
        .setId(inkBasic.getId())
        .setType(inkBasic.getType())
        .setDescription(inkBasic.getDescription())
        .setFeatures(inkBasic.getFeatures())
        .setDots(inkBasic.getDots())
        .setMarks(inkBasic.getMarks())
        .setParticipants(inkBasic.getParticipants())
        .setRefs(inkBasic.getRefs())
        .setTitle(inkBasic.getTitle());

    return hydrateEx(inkBasic);
  }


}
