package general;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Comprobaciones {
	
	public static boolean esIntValido(String numInt, boolean nulable) {
		if(esNulo(numInt)) {
			return nulable;
		}
		
		if(numInt.length()<=9 && numInt.matches("[0-9]+")) {	//La aplicación no utiliza números negativos
			return true;
		}
		return false;
	}
	
    public static boolean esDecimalValido(String numDecimal, int precision, int escala, boolean nulable) {
		if(esNulo(numDecimal)) {
			return nulable;
		}
    	
    	try {
            BigDecimal decimal = new BigDecimal(numDecimal);
            BigDecimal zero = BigDecimal.ZERO;
            
            if(decimal.compareTo(zero) < 0) {	//La aplicación no utiliza números negativos
            	return false;
            } if (decimal.precision() <= precision && decimal.scale() <= escala) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }


	public static boolean esStringValido(String texto, int longitud, boolean nulable) {
		if(esNulo(texto)) {
			return nulable;
		} 
		
		return texto.length()<longitud;
	}
	
	
	public static boolean esFechaValida(String fecha, String formato, boolean nulable) {
		if(esNulo(fecha)) {
			return nulable;
		}
		
		if(formato.equals("")) {
			formato = "yyyy-MM-dd";
		}
		
		DateTimeFormatter formateador = DateTimeFormatter.ofPattern(formato);
		
		try {
			LocalDate.parse(fecha, formateador);
			return true;
		} catch(DateTimeParseException e) {
			return false;
		}
	}

	//Un textField vacío devuelve "", por lo que se considerará nulo
	public static boolean esNulo(String valor) {
		if(valor.equals(null) || valor.trim().isEmpty()) {
			return true;
		}
		return false;
	}
}
