package kouch.annotations

@MustBeDocumented
@Target(AnnotationTarget.PROPERTY)
annotation class FieldName(val name: String)
