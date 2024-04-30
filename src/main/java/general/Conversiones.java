package general;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Conversiones {
	public static Timestamp textoEnTimestamp(String fecha) {
		fecha += " 00:00:00";
		String formato = "yyyy-MM-dd HH:mm:ss";
		if(Comprobaciones.esFechaValida(fecha, formato, false)) {
			DateTimeFormatter formateador = DateTimeFormatter.ofPattern(formato);
			LocalDateTime fechaFormateada = LocalDateTime.parse(fecha, formateador);
			return Timestamp.valueOf(fechaFormateada);
		}
		
		return null;
	}
	
	public static String textoVacioEnNull(String texto) {
	    // Verifica si el texto es null o está vacío
	    if (texto == null || texto.trim().isEmpty()) {
	        return null;
	    } else {
	        return texto;
	    }
	}

	
	public static Integer textoEnIntONull(String num) {
	    try {
	        return Integer.parseInt(num);
	    } catch (NumberFormatException e) {
	        return null;
	    }
	}
	
	public static BigDecimal textoEnDecimalONull(String num) {
		try {
			return new BigDecimal(num);
		} catch (NumberFormatException e){
			return null;
		}
	}
	
    public static String convertirMilisegundos(long milisegundos) {
        long segundos = milisegundos / 1000;
        long minutos = segundos / 60;
        segundos = segundos % 60;
        
        return minutos + " min " + segundos + " s";
    }

	
	public static int convertirAMilisegundos(int minutos, int segundos) {
		try {
			int minutosEnMilisegundos = minutos * 60 * 1000;
			int segundosEnMilisegundos = segundos * 1000;
			
			return minutosEnMilisegundos + segundosEnMilisegundos;
		} catch(IllegalArgumentException iae) {
			return Integer.MAX_VALUE;
		}

	}
	
	
	public static int convertirAMilisegundos(String XminYs) {
	    Pattern pattern = Pattern.compile("(\\d+) min (\\d+) s");
	    Matcher matcher = pattern.matcher(XminYs);
	    
	    if (matcher.find()) {
	        int min = Integer.parseInt(matcher.group(1));
	        int s = Integer.parseInt(matcher.group(2));
	        
			try {
				int minutosEnMilisegundos = min * 60 * 1000;
				int segundosEnMilisegundos = s * 1000;
				
				return minutosEnMilisegundos + segundosEnMilisegundos;
			} catch(IllegalArgumentException iae) {
				return Integer.MAX_VALUE;
			}
			
	    } else {
	    	return 0;
	    }
	}
	
	

	
	
	public static String cambiarFormatoFecha(String fecha, String formatoActual, String formatoDeseado) {
		try {
	        DateTimeFormatter formatoOriginal = DateTimeFormatter.ofPattern(formatoActual);
	        LocalDate localDate = LocalDate.parse(fecha, formatoOriginal);
	
	        DateTimeFormatter formatoNuevo = DateTimeFormatter.ofPattern(formatoDeseado);
	        String fechaFormateada = localDate.format(formatoNuevo);
	        
	        return fechaFormateada;
		} catch(DateTimeParseException e) {
			return null;
		}
	}
}
