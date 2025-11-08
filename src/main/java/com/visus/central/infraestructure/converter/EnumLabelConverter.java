/**
 * Conversor abstracto para ENUMS que utilizan un sistema de etiquetas (labels)
 * personalizadas para persistencia en base de datos.
 * 
 * <p>Esta clase proporciona una implementación base para convertir ENUMS hacia y desde
 * valores de cadena utilizando métodos personalizados {@code getLabel()} y {@code fromLabel()}
 * en lugar de los nombres predeterminados de los ENUMS.</p>
 *
 * <p><b>Requisitos para el enum:</b></p>
 * <ul>
 *   <li>Debe tener un método {@code String getLabel()} que retorne la representación 
 *       para persistencia</li>
 *   <li>Debe tener un método estático {@code fromLabel(String)} que convierta la cadena
 *       de vuelta al valor del ENUM</li>
 * </ul>
 *
 * @param <E> el tipo de enum que será convertido, debe extender {@code Enum<E>}
 * @author berthasuer
 * @version 1.0
 * @since 1.0
 * @see AttributeConverter
 */
package com.visus.central.infraestructure.converter;

import jakarta.persistence.AttributeConverter;
import java.lang.reflect.Method;


public abstract class EnumLabelConverter <E extends Enum<E>> implements AttributeConverter<E, String> {

	private final Class<E> enumClass;
	
	/**
     * Construye un nuevo conversor para el enum especificado.
     *
     * @param enumClass la clase del enum que será convertido
     * @throws NullPointerException si {@code enumClass} es null
     */

    protected EnumLabelConverter(Class<E> enumClass) {
        this.enumClass = enumClass;
    }
    
    /**
     * Convierte el valor del enum a su representación en base de datos.
     * 
     * <p>Invoca el método {@code getLabel()} del enum para obtener la cadena
     * que será persistida en la base de datos.</p>
     *
     * @param attribute el valor del enum a convertir, puede ser {@code null}
     * @return la cadena representando el enum para persistencia, o {@code null} si 
     *         el atributo es {@code null}
     * @throws IllegalStateException si el enum no tiene un método {@code getLabel()} 
     *         accesible o si ocurre un error durante la invocación
     */
    
    @Override
    public String convertToDatabaseColumn(E attribute) {
        if (attribute == null) return null;
        try {
            Method getLabel = enumClass.getMethod("getLabel");
            return (String) getLabel.invoke(attribute);
        } catch (Exception ex) {
            throw new IllegalStateException("El enum " + enumClass.getSimpleName() + " debe tener un método getLabel()");
        }
    }

    /**
     * Convierte el valor de base de datos de vuelta al enum correspondiente.
     * 
     * <p>Invoca el método estático {@code fromLabel(String)} del enum para reconstruir
     * la instancia del enum a partir de la cadena persistida.</p>
     *
     * @param dbData la cadena desde la base de datos, puede ser {@code null}
     * @return la instancia del enum correspondiente, o {@code null} si {@code dbData} 
     *         es {@code null}
     * @throws IllegalArgumentException si {@code dbData} no corresponde a ningún 
     *         valor del enum o si ocurre un error durante la conversión
     */
    
    @SuppressWarnings("unchecked")
	@Override
    public E convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            Method fromLabel = enumClass.getMethod("fromLabel", String.class);
            return (E) fromLabel.invoke(null, dbData);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Valor inválido para " + enumClass.getSimpleName() + ": " + dbData);
        }
    }


}
