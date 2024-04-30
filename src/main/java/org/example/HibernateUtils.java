package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.util.List;

public class HibernateUtils {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().buildSessionFactory(
                    new StandardServiceRegistryBuilder().configure().build());
        
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed. " + ex);
//            return new Configuration().configure().buildSessionFactory(
//                    new StandardServiceRegistryBuilder().configure().build());
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    
    public static void iniciar() {
    	getSessionFactory();
    }

    
    //FUNCIONES CLIENTES
    public static List<CustomerClass> buscarClientes(String campoFiltro, String filtro) {
    	List<CustomerClass> resultados = null;
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
        	Transaction tx = session.beginTransaction();
        	
        	Query<CustomerClass> query = session.createQuery(
                    "SELECT customer " +
                    "FROM CustomerClass customer " +
                    "WHERE "+campoFiltro+" = :filtro", CustomerClass.class);

            query.setParameter("filtro", filtro);
            resultados = query.list();
            
            tx.commit();
        } catch (Exception e) {
        	return null;
        }

		return resultados;
    }
    
    public static List<CustomerClass> buscarTodosClientes() {
    	List<CustomerClass> resultados = null;
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
        	Transaction tx = session.beginTransaction();
        	
        	Query<CustomerClass> query = session.createQuery(
                    "SELECT customer " +
                    "FROM CustomerClass customer", CustomerClass.class);
        	
            resultados = query.list();
            
            tx.commit();
        } catch (Exception e) {
        	return null;
        }

		return resultados;
    }
    
    public static List<CustomerClass> buscarClientesPorApellido(String apellido) {
    	return buscarClientes("customer.lastName", apellido);
    }
    
    public static List<CustomerClass> buscarClientesPorId(String id) {
    	return buscarClientes("customer.customerId", id);
    }
    
    public static int crearCliente(CustomerClass nuevoCliente) {
    	int idCliente = primerIdDisponible("customerId", "CustomerClass");
    	Transaction tx = null;
    	
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
    		tx = session.beginTransaction();
    		
    		if(idCliente != -1) {
	    		nuevoCliente.setCustomerId(idCliente);
	    		session.persist(nuevoCliente);
	    		tx.commit();
	    	}
		} catch (Exception e) {
	    	if(tx != null) {
	    		tx.rollback();
	    	}
	    	
	    	return -1;
	    }
    	
    	return idCliente;
    }
    
    public static boolean actualizarCliente(CustomerClass editadoCliente) {
    	Transaction tx = null;
    	
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
    		tx = session.beginTransaction();

    		CustomerClass customer = session.get(CustomerClass.class, editadoCliente.getCustomerId());
    		customer.setFirstName(editadoCliente.getFirstName());
    		customer.setLastName(editadoCliente.getLastName());
    		customer.setCompany(editadoCliente.getCompany());
    		customer.setAddress(editadoCliente.getAddress());
    		customer.setCity(editadoCliente.getCity());
    		customer.setState(editadoCliente.getState());
    		customer.setCountry(editadoCliente.getCountry());
    		customer.setPostalCode(editadoCliente.getPostalCode());
    		customer.setPhone(editadoCliente.getPhone());
    		customer.setFax(editadoCliente.getFax());
    		customer.setEmail(editadoCliente.getEmail());
    		customer.setSupportRepId(editadoCliente.getSupportRepId());
    		customer.setEntryDate(editadoCliente.getEntryDate());

    		tx.commit();
		} catch (Exception e) {
	    	if(tx != null) {
	    		tx.rollback();
	    	}
	    	
	    	return false;
	    }
    	
		return true;
    }
    
   
    //FUNCIONES EMPLEADOS
    public static List<EmployeeClass> buscarEmpleado(String campoFiltro, String filtro) {
    	List<EmployeeClass> resultados = null;
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
        	Transaction tx = session.beginTransaction();
        	
        	Query<EmployeeClass> query = session.createQuery(
                    "SELECT employee " +
                    "FROM EmployeeClass employee " +
                    "WHERE "+campoFiltro+" = :filtro", EmployeeClass.class);

            query.setParameter("filtro", filtro);
            resultados = query.list();
            
            tx.commit();
        } catch (Exception e) {
        	return null;
        }

		return resultados;
    }
    
    
    public static List<EmployeeClass> buscarEmpleadoPorApellido(String apellido){
    	return buscarEmpleado("employee.lastName", apellido);
    }
    
    public static List<EmployeeClass> buscarEmpleadoPorPuesto(String puesto){
    	return buscarEmpleado("employee.title", puesto);
    }
    
    public static List<EmployeeClass> buscarEmpleadoPorId(String employeeId) {
    	return buscarEmpleado("employee.employeeId", employeeId);
    }
    
    public static int crearEmpleado(EmployeeClass nuevoEmpleado) {
    	int idEmpleado = primerIdDisponible("employeeId", "EmployeeClass");
    	Transaction tx = null;
    	
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
    		tx = session.beginTransaction();
    		
    		if(idEmpleado != -1) {
	    		nuevoEmpleado.setEmployeeId(idEmpleado);
	    		session.persist(nuevoEmpleado);
	    		tx.commit();
	    	}
		} catch (Exception e) {
	    	if(tx != null) {
	    		tx.rollback();
	    	}
	    	return -1;
	    }
    	return idEmpleado;
    }
    
    //update está deprecado para Hibernate6, y merge crearía un registro nuevo de no encontrar el id
    public static boolean actualizarEmpleado(EmployeeClass editadoEmpleado) {
    	Transaction tx = null;
    	
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
    		tx = session.beginTransaction();

    		EmployeeClass employee = session.get(EmployeeClass.class, editadoEmpleado.getEmployeeId());
    		employee.setFirstName(editadoEmpleado.getFirstName());
    		employee.setLastName(editadoEmpleado.getLastName());
    		employee.setTitle(editadoEmpleado.getTitle());
    		employee.setReportsTo(editadoEmpleado.getReportsTo());
    		employee.setBirthDate(editadoEmpleado.getBirthDate());
    		employee.setHireDate(editadoEmpleado.getHireDate());
    		employee.setAddress(editadoEmpleado.getAddress());
    		employee.setCity(editadoEmpleado.getCity());
    		employee.setState(editadoEmpleado.getState());
    		employee.setCountry(editadoEmpleado.getCountry());
    		employee.setPostalCode(editadoEmpleado.getPostalCode());
    		employee.setPhone(editadoEmpleado.getPhone());
    		employee.setFax(editadoEmpleado.getFax());
    		employee.setEmail(editadoEmpleado.getEmail());
 
    		tx.commit();
		} catch (Exception e) {
	    	if(tx != null) {
	    		tx.rollback();
	    	}
	    	
	    	return false;
	    }
    	
		return true;
    }

	public static List<EmployeeClass> buscarTodosEmpleados() {
    	List<EmployeeClass> resultados = null;
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
        	Transaction tx = session.beginTransaction();
        	
        	Query<EmployeeClass> query = session.createQuery(
                    "SELECT employee " +
                    "FROM EmployeeClass employee", EmployeeClass.class);
        	
            resultados = query.list();
            
            tx.commit();
        } catch (Exception e) {
        	return null;
        }

		return resultados;
	}
    
    
    //FUNCIONES FACTURACIÓN
    public static List<Object[]> buscarFactura(String campoFiltro, String filtro) {
    	List<Object[]> resultados = null;
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
        	Transaction tx = session.beginTransaction();
        	
        	Query<Object[]> query = session.createQuery(
                    "SELECT invoice, customer.firstName, customer.lastName " +
                    "FROM InvoiceClass invoice " +
                    "JOIN CustomerClass customer ON " +
                    "invoice.customerId = customer.customerId " +
                    "WHERE "+campoFiltro+" = :filtro", Object[].class);

            query.setParameter("filtro", filtro);
            resultados = query.list();
            
            tx.commit();
        } catch (Exception e) {
        	return null;
        }

		return resultados;
    }
    
    
    public static List<Object[]> buscarFacturaPorClienteId(String clienteId) {
    	return buscarFactura("invoice.customerId", clienteId);
    }
    
    public static List<Object[]> buscarFacturaPorFacturaId(String facturaId) {
    	return buscarFactura("invoice.invoiceId", facturaId);
    }
    
    public static List<Object[]> buscarLineasDeFactura(String facturaId) {
    	List<Object[]> resultados = null;
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
        	Transaction tx = session.beginTransaction();
        	
        	Query<Object[]> query = session.createQuery(
        			"SELECT track.name, album.title, invoiceline.unitPrice, invoiceline.quantity, invoiceline.unitPrice*invoiceline.quantity " +
        			"FROM TrackClass track " + 
        			"JOIN AlbumClass album ON track.albumId = album.albumId " +
        			"JOIN InvoiceLineClass invoiceline ON track.trackId = invoiceline.trackId " +
                    "WHERE invoiceline.invoiceId = :filtro", Object[].class);

            query.setParameter("filtro", facturaId);
            resultados = query.list();
            
            tx.commit();
        } catch (Exception e) {
        	return null;
        }

		return resultados;
    }
    
    public static List<Object[]> buscarTemasParaFactura() {
    	List<Object[]> resultados = null;
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
        	Transaction tx = session.beginTransaction();
        	
        	Query<Object[]> query = session.createQuery(
        			"SELECT track.trackId, track.name, album.title, track.unitPrice " +
        			"FROM TrackClass track " + 
        			"JOIN AlbumClass album ON track.albumId = album.albumId", Object[].class);
        	
            resultados = query.list();
            
            tx.commit();
        } catch (Exception e) {
        	return null;
        }

		return resultados;
    }
    
    public static int crearFactura(InvoiceClass nuevaFactura) {
    	int idFactura = primerIdDisponible("invoiceId", "InvoiceClass");
    	Transaction tx = null;
    	
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
    		tx = session.beginTransaction();
    		
    		if(idFactura != -1) {
    			nuevaFactura.setInvoiceId(idFactura);
	    		session.persist(nuevaFactura);
	    		tx.commit();
	    	}
		} catch (Exception e) {
	    	if(tx != null) {
	    		tx.rollback();
	    	}
	    	return -1;
	    }
    	return idFactura;
    }
    
    //SI SE ESTÁN CREANDO, Workbench por defecto muestra 1000 primeras y hay huecos desde 2240
    public static int crearLineaFactura(InvoiceLineClass nuevaLinea) {
    	int idLineaFactura = primerIdDisponible("invoiceLineId", "InvoiceLineClass");
    	Transaction tx = null;
    	
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
    		tx = session.beginTransaction();
    		
    		if(idLineaFactura != -1) {
    			nuevaLinea.setInvoiceLineId(idLineaFactura);
	    		session.persist(nuevaLinea);
	    		tx.commit();
	    	}
		} catch (Exception e) {
	    	if(tx != null) {
	    		tx.rollback();
	    	}
	    	return -1;
	    }
    	return idLineaFactura;
    }
    
    public static boolean actualizarFactura(InvoiceClass editadaFactura) {
    	Transaction tx = null;
    	
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
    		tx = session.beginTransaction();

    		InvoiceClass invoice = session.get(InvoiceClass.class, editadaFactura.getInvoiceId());
    		
    		invoice.setInvoiceId(editadaFactura.getInvoiceId());
			invoice.setCustomerId(editadaFactura.getCustomerId());
			invoice.setInvoiceDate(editadaFactura.getInvoiceDate());
			invoice.setBillingAddress(editadaFactura.getBillingAddress());
			invoice.setBillingCity(editadaFactura.getBillingCity());
			invoice.setBillingState(editadaFactura.getBillingState());
			invoice.setBillingCountry(editadaFactura.getBillingCountry());
			invoice.setBillingPostalCode(editadaFactura.getBillingPostalCode());
			invoice.setTotal(editadaFactura.getTotal());
 
    		tx.commit();
		} catch (Exception e) {
	    	if(tx != null) {
	    		tx.rollback();
	    	}
	    	return false;
	    }
		return true;
    }
    
    
    //FUNCIONES TEMAS
    public static List<Object[]> buscarCancion(String campoFiltro, String filtro) {
    	List<Object[]> resultados = null;
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
        	Transaction tx = session.beginTransaction();
        	
        	Query<Object[]> query = session.createQuery(
                    "SELECT track, album.title, album.artistId, artist.name, genre.name, mediaType.name " +
                    "FROM TrackClass track " +
                    "JOIN AlbumClass album ON track.albumId = album.albumId " +
                    "JOIN ArtistClass artist ON album.artistId = artist.artistId " +
                    "JOIN GenreClass genre ON track.genreId = genre.genreId " +
                    "JOIN MediaTypeClass mediaType ON track.mediaTypeId = mediaType.mediaTypeId " +
                    "WHERE "+campoFiltro+" = :filtro", Object[].class);

            query.setParameter("filtro", filtro);
            resultados = query.list();
            
            tx.commit();
        } catch (Exception e) {
        	return null;
        }

		return resultados;
    }
    
    public static List<Object[]> buscarCancionPorNombre(String nombreCancion) {
    	return buscarCancion("track.name", nombreCancion);
    }
    
    public static List<Object[]> buscarCancionPorArtista(String nombreArtista) {
    	return buscarCancion("artist.name", nombreArtista);
    }
    
    public static List<Object[]> buscarCancionPorCompositor(String compositor) {
    	return buscarCancion("track.composer", compositor);
    }
    
    public static List<Object[]> buscarCancionPorAlbum(String nombreAlbum) {
    	return buscarCancion("album.title", nombreAlbum);
    }
    
    public static List<Object[]> buscarCancionPorGenero(String nombreGenero) {
        return buscarCancion("genre.name", nombreGenero);
    }
    
    public static List<Object[]> buscarCancionPorDuracion(String duracion) {
    	return buscarCancion("track.milliseconds", duracion);
    }
    
    public static List<Object[]> buscarCancionPorId(String id) {
    	return buscarCancion("track.trackId", id);
    }
    
    public static List<Object[]> buscarCancionesEnPlaylist(String playlistId) {
    	List<Object[]> resultados = null;
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
        	Transaction tx = session.beginTransaction();
        	
        	Query<Object[]> query = session.createQuery(
        			"SELECT track.trackId, track.name, album.title, track.milliseconds " +
        			"FROM PlaylistTrackClass playlisttrack " +
        			"JOIN TrackClass track ON playlisttrack.trackId = track.trackId " +
        			"JOIN AlbumClass album ON track.albumId = album.albumId " +
        			"WHERE playlistId = :filtro", Object[].class);

            query.setParameter("filtro", playlistId);
            resultados = query.list();
            
            tx.commit();
        } catch (Exception e) {
        	return null;
        }

		return resultados;
    }
    
    public static int crearCancion(TrackClass nuevaCancion) {
    	int idCancion = primerIdDisponible("trackId", "TrackClass");
    	Transaction tx = null;
    	
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
    		tx = session.beginTransaction();
    		
    		if(idCancion != -1) {
    			System.out.println("MediaTypeID:" +nuevaCancion.getMediaTypeId());
    			nuevaCancion.setTrackId(idCancion);
	    		session.persist(nuevaCancion);
	    		tx.commit();
	    	}
		} catch (Exception e) {
			System.out.println(e);
	    	if(tx != null) {
	    		tx.rollback();  //Si algo sale mal evita inconsistencia
	    	}
	    	return -1;
	    }
    	return idCancion;
    }
    
    public static boolean actualizarCancion(TrackClass editadoTema) {
    	Transaction tx = null;
    	
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
    		tx = session.beginTransaction();

    		TrackClass tema = session.get(TrackClass.class, editadoTema.getTrackId());
    		tema.setName(editadoTema.getName());
    		tema.setComposer(editadoTema.getComposer());
    		tema.setUnitPrice(editadoTema.getUnitPrice());

    		tx.commit();
		} catch (Exception e) {
	    	if(tx != null) {
	    		tx.rollback();
	    	}
	    	
	    	return false;
	    }
    	
		return true;
    }
    
    //FUNCIONES LISTAS
    public static List<Object[]> buscarPlaylist(String campoFiltro, String filtro) {
    	List<Object[]> resultados = null;
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
        	Transaction tx = session.beginTransaction();
        	
        	Query<Object[]> query = session.createQuery(
                    "SELECT playlist, COALESCE(SUM(track.milliseconds), 0) " +
                    "FROM PlaylistClass playlist " +
                    "LEFT JOIN PlaylistTrackClass playlisttrack ON playlist.playlistId = playlisttrack.playlistId " +
                    "LEFT JOIN TrackClass track ON playlisttrack.trackId = track.trackId " +
                    "WHERE "+campoFiltro+" = :filtro " +
                    "GROUP BY playlist.playlistId, playlist.name", Object[].class);

            query.setParameter("filtro", filtro);
            resultados = query.list();
            
            tx.commit();
        } catch (Exception e) {
        	return null;
        }

		return resultados;
    }
    
    public static List<Object[]> buscarPlaylistPorPlaylistId(String listaId) {
    	return buscarPlaylist("playlist.playlistId", listaId);
    }
    
    public static List<Object[]> buscarPlaylistPorNombre(String nombrePlaylist) {
    	return buscarPlaylist("playlist.name", nombrePlaylist);
    }
 
    //Busqueda reversa
    public static List<PlaylistClass> buscarPlaylistsConTrackId(String cancionId) {
    	List<PlaylistClass> resultados = null;
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
        	Transaction tx = session.beginTransaction();
        	
        	Query<PlaylistClass> query = session.createQuery(
        			"SELECT playlist FROM PlaylistClass playlist " +
        			"JOIN PlaylistTrackClass playlisttrack ON playlist.playlistId = playlisttrack.playlistId " +
        			"JOIN TrackClass track ON track.trackId = playlisttrack.trackId " +
        			"WHERE track.trackId = :filtro", PlaylistClass.class);

            query.setParameter("filtro", cancionId);
            resultados = query.list();
            
            tx.commit();
        } catch (Exception e) {
        	return null;
        }

		return resultados;
    }
    
    
    public static List<Object[]> buscarCancionesParaLista() {
    	List<Object[]> resultados = null;
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
        	Transaction tx = session.beginTransaction();
        	
        	Query<Object[]> query = session.createQuery(
        			"SELECT DISTINCT track.trackId, track.name, album.title, track.milliseconds " +
        			"FROM PlaylistTrackClass playlisttrack " +
        			"JOIN TrackClass track ON playlisttrack.trackId = track.trackId " +
        			"JOIN AlbumClass album ON track.albumId = album.albumId ", Object[].class);

            resultados = query.list();
            
            tx.commit();
        } catch (Exception e) {
        	return null;
        }

		return resultados;
    }
    
    public static int crearPlaylist(PlaylistClass nuevaLista) {
    	int idLista = primerIdDisponible("playlistId", "PlaylistClass");
    	Transaction tx = null;
    	
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
    		tx = session.beginTransaction();
    		
    		if(idLista != -1) {
    			nuevaLista.setPlaylistId(idLista);
	    		session.persist(nuevaLista);
	    		tx.commit();
	    	}
		} catch (Exception e) {
	    	if(tx != null) {
	    		tx.rollback();
	    	}
	    	
	    	return -1;
	    }
    	
    	return idLista;
    }
    
    public static boolean crearPlaylistTrack(int playlistId, int trackId) {
    	Transaction tx = null;
    	
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
    		tx = session.beginTransaction();

    		PlaylistTrackClass nuevaCancionLista = new PlaylistTrackClass();
			nuevaCancionLista.setPlaylistId(playlistId);
			nuevaCancionLista.setTrackId(trackId);
    		session.persist(nuevaCancionLista);
    		tx.commit();
		} catch (Exception e) {
	    	if(tx != null) {
	    		tx.rollback();
	    	}
	    	return false;
	    }
    	return true;
    }
    
    public static boolean actualizarPlaylist(PlaylistClass editadaLista) {
    	Transaction tx = null;
    	
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
    		tx = session.beginTransaction();

    		PlaylistClass playlist = session.get(PlaylistClass.class, editadaLista.getPlaylistId());
    		playlist.setName(editadaLista.getName());

    		tx.commit();
		} catch (Exception e) {
	    	if(tx != null) {
	    		tx.rollback();
	    	}
	    	
	    	return false;
	    }
    	
		return true;
    }
    
	public static boolean yaExistePlaylistTrack(int playlistId, int trackId) {
    	List<PlaylistTrackClass> resultados = null;
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
        	Transaction tx = session.beginTransaction();
        	
        	System.out.println(playlistId);
        	System.out.println(trackId);
        	Query<PlaylistTrackClass> query = session.createQuery(
        			"SELECT playlistTrack " +
        			"FROM PlaylistTrackClass playlistTrack " +
        			"WHERE playlistId = :filtro1 AND trackId = :filtro2", PlaylistTrackClass.class);

        	query.setParameter("filtro1", playlistId);
        	query.setParameter("filtro2", trackId);
            resultados = query.list();
            
            
            if(resultados == null || resultados.isEmpty()) {
            	return false;
            } else if(resultados.size()<1) {
            	return false;
            }
            
            tx.commit();
        } catch (Exception e) {
        	return false;
        }

		return true;
	}
    
    //FUNCIONES ARTISTAS
    public static ArtistClass buscarArtistaPorId(int artistId) {
    	ArtistClass resultados = null;
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
        	Transaction tx = session.beginTransaction();
        	
        	Query<ArtistClass> query = session.createQuery(
                    "SELECT artist " +
                    "FROM ArtistClass artist " +
                    "WHERE artistId = :filtro", ArtistClass.class);
        	
            query.setParameter("filtro", artistId);
            resultados = query.getSingleResult();
            
            tx.commit();
        } catch (Exception e) {
            return null;
        }

		return resultados;
    }
    
    public static List<ArtistClass> buscarArtista(String campoFiltro, String filtro) {
    	List<ArtistClass> resultados = null;
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
        	Transaction tx = session.beginTransaction();
        	
        	Query<ArtistClass> query = session.createQuery(
                    "SELECT artist " +
                    "FROM ArtistClass artist" +
                    "WHERE "+campoFiltro+" = :filtro", ArtistClass.class);

            query.setParameter("filtro", filtro);
            resultados = query.list();
            
            tx.commit();
        } catch (Exception e) {
            return null;
        }

		return resultados;
    }
    
    public static List<ArtistClass> buscarTodosArtistas() {
    	List<ArtistClass> resultados = null;
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
        	Transaction tx = session.beginTransaction();
        	
        	Query<ArtistClass> query = session.createQuery(
                    "SELECT artist " +
                    "FROM ArtistClass artist", ArtistClass.class);
        	
            resultados = query.list();
            tx.commit();
        } catch (Exception e) {
            return null;
        }

		return resultados;
    }
    
	public static boolean existeArtistaPorId(Integer artistId) {
        List<ArtistClass> resultados = null;
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
        	Query<ArtistClass> query = session.createQuery(
        			"SELECT artist " +
        			"FROM ArtistClass artist " +
        			"WHERE artistId = :filtro", ArtistClass.class);  
       
            query.setParameter("filtro", artistId);
            resultados = query.list();
            
            tx.commit();
        } catch (Exception e){
    		return false;
        }
        
        return resultados != null && !resultados.isEmpty();	
	}
	
    public static int crearArtista(ArtistClass nuevoArtista) {
    	int idArtista = primerIdDisponible("artistId", "ArtistClass");
    	Transaction tx = null;
    	
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
    		tx = session.beginTransaction();
    		
    		if(idArtista != -1) {
    			nuevoArtista.setArtistId(idArtista);
	    		session.persist(nuevoArtista);
	    		tx.commit();
	    	}
		} catch (Exception e) {
	    	if(tx != null) {
	    		tx.rollback();
	    	}
	    	
	    	return -1;
	    }
    	
    	return idArtista;
    }

    
    //FUNCIONES ALBUMS
    public static List<AlbumClass> buscarTodosAlbumes() {
    	List<AlbumClass> resultados = null;
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
        	Transaction tx = session.beginTransaction();
        	
        	Query<AlbumClass> query = session.createQuery(
                    "SELECT album " +
                    "FROM AlbumClass album", AlbumClass.class);
        	
            resultados = query.list();
            
            tx.commit();
        } catch (Exception e) {
            return null;
        }

		return resultados;
    }
    
    public static List<Object[]> buscarAlbum(String campoFiltro, String filtro) {
    	List<Object[]> resultados = null;
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
        	Transaction tx = session.beginTransaction();
        	
        	Query<Object[]> query = session.createQuery(
                    "SELECT album, artist.Name " +
                    "FROM AlbumClass album " +
                    "JOIN ArtistClass artist ON album.artistId = artist.artistId " +
                    "WHERE "+campoFiltro+" = :filtro", Object[].class);

            query.setParameter("filtro", filtro);
            resultados = query.list();
            
            tx.commit();
        } catch (Exception e) {
            return null;
        }

		return resultados;
    }
    
    public static List<Object[]> buscarAlbumPorNombre(String nombreAlbum) {
    	return buscarAlbum("album.title", nombreAlbum);
    }
    
    public static List<Object[]> buscarAlbumPorArtista(String nombreArtista) {
    	return buscarAlbum("artist.name", nombreArtista);
    }
    
	public static boolean existeAlbumPorId(Integer albumId) {
        List<AlbumClass> resultados = null;
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
        	Query<AlbumClass> query = session.createQuery(
        			"SELECT album " +
        			"FROM AlbumClass album " +
        			"WHERE albumId = :filtro", AlbumClass.class);  
       
            query.setParameter("filtro", albumId);
            resultados = query.list();
            
            tx.commit();
        } catch (Exception e){
    		return false;
        }
        
        return resultados != null && !resultados.isEmpty();	
	}
	
    public static int crearAlbum(AlbumClass nuevoAlbum) {
    	int idAlbum = primerIdDisponible("albumId", "AlbumClass");
    	Transaction tx = null;
    	
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
    		tx = session.beginTransaction();
    		
    		if(idAlbum != -1) {
    			nuevoAlbum.setAlbumId(idAlbum);
	    		session.persist(nuevoAlbum);
	    		tx.commit();
	    	}
		} catch (Exception e) {
	    	if(tx != null) {
	    		tx.rollback();
	    	}
	    	
	    	return -1;
	    }
    	
    	return idAlbum;
    }
    
    public static boolean esAlbumDeArtistaId(Integer albumId, int artistaId) {
        List<AlbumClass> resultados = null;
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
        	Query<AlbumClass> query = session.createQuery(
        			"SELECT album " +
        			"FROM AlbumClass album " +
        			"WHERE albumId = :filtro1 AND artistId = :filtro2", AlbumClass.class);  
       
            query.setParameter("filtro1", albumId);
            query.setParameter("filtro2", artistaId);
            resultados = query.list();
            
            tx.commit();
        } catch (Exception e){
    		return false;
        }
        
        return resultados != null && !resultados.isEmpty();	
    }

    //FUNCIONES GENEROS
    public static List<GenreClass> buscarGeneros(String campoFiltro, String filtro) {
    	List<GenreClass> resultados = null;
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
        	Transaction tx = session.beginTransaction();
        	
        	Query<GenreClass> query = session.createQuery(
                    "SELECT genre " +
                    "FROM GenreClass customer " +
                    "WHERE "+campoFiltro+" = :filtro", GenreClass.class);

            query.setParameter("filtro", filtro);
            resultados = query.list();
            
            tx.commit();
        } catch (Exception e) {
            return null;
        }

		return resultados;
    }
    
    public static List<GenreClass> buscarTodosGeneros() {
    	List<GenreClass> resultados = null;
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
        	Transaction tx = session.beginTransaction();
        	
        	Query<GenreClass> query = session.createQuery(
                    "SELECT genre " +
                    "FROM GenreClass genre", GenreClass.class);
        	
            resultados = query.list();
            tx.commit();
        } catch (Exception e) {
            return null;
        }

		return resultados;
    }
    
	public static boolean existeGeneroPorId(Integer genreId) {
        List<GenreClass> resultados = null;
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
        	Query<GenreClass> query = session.createQuery(
        			"SELECT genre " +
        			"FROM GenreClass genre " +
        			"WHERE genreId = :filtro", GenreClass.class);  
       
            query.setParameter("filtro", genreId);
            resultados = query.list();
            
            tx.commit();
        } catch (Exception e){
    		return false;
        }
        
        return resultados != null && !resultados.isEmpty();	
	}
	
    public static int crearGenero(GenreClass nuevoGenero) {
    	int idGenero = primerIdDisponible("genreId", "GenreClass");
    	Transaction tx = null;
    	
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
    		tx = session.beginTransaction();
    		
    		if(idGenero != -1) {
    			nuevoGenero.setGenreId(idGenero);
	    		session.persist(nuevoGenero);
	    		tx.commit();
	    	}
		} catch (Exception e) {
	    	if(tx != null) {
	    		tx.rollback();
	    	}
	    	
	    	return -1;
	    }
    	
    	return idGenero;
    }
    
    //FUNCIONES SOPORTE
    public static List<MediaTypeClass> buscarTodosSoportes() {
    	List<MediaTypeClass> resultados = null;
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
        	Transaction tx = session.beginTransaction();
        	
        	Query<MediaTypeClass> query = session.createQuery(
                    "SELECT mediaType " +
                    "FROM MediaTypeClass mediaType", MediaTypeClass.class);
        	
            resultados = query.list();
            tx.commit();
        } catch (Exception e) {
            return null;
        }

		return resultados;
    }
    
	public static boolean existeSoportePorId(Integer mediaTypeId) {
        List<MediaTypeClass> resultados = null;
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
        	Query<MediaTypeClass> query = session.createQuery(
        			"SELECT mediaType " +
        			"FROM MediaTypeClass mediaType " +
        			"WHERE mediaTypeId = :filtro", MediaTypeClass.class);  
       
            query.setParameter("filtro", mediaTypeId);
            resultados = query.list();
            
            tx.commit();
        } catch (Exception e){
    		return false;
        }
        
        return resultados != null && !resultados.isEmpty();	
	}
	
    public static int crearSoporte(MediaTypeClass nuevoSoporte) {
    	int idSoporte = primerIdDisponible("mediaTypeId", "MediaTypeClass");
    	Transaction tx = null;
    	
    	try(Session session = HibernateUtils.getSessionFactory().openSession()) {
    		tx = session.beginTransaction();
    		
    		if(idSoporte != -1) {
    			nuevoSoporte.setMediaTypeId(idSoporte);
	    		session.persist(nuevoSoporte);
	    		tx.commit();
	    	}
		} catch (Exception e) {
	    	if(tx != null) {
	    		tx.rollback();
	    	}
	    	
	    	return -1;
	    }
    	
    	return idSoporte;
    }
    
    
    public static int primerIdDisponible(String nombreCampoId, String tablaClass) {
        SessionFactory sf = HibernateUtils.getSessionFactory();
        try(Session session = sf.openSession()) {
            Transaction tx = session.beginTransaction();
            Query<Integer> query = session.createQuery("SELECT "+nombreCampoId+" FROM "+tablaClass+" ORDER BY "+nombreCampoId, Integer.class);
            List<Integer> idsExisten = query.list();

            int mayorId = idsExisten.get(idsExisten.size() -1);
            for(int i=1; i<=mayorId; i++) {
                if(!idsExisten.contains(i)) {
                    //El primer hueco libre
                    tx.commit();
                    return i;
                }
            }
            tx.commit();
            //No hay huecos, devuelve el siguiente del último
            return mayorId +1;
        } catch (Throwable i){
            return -1;
        }
    }
    
    
    //Listados
    public static void CancionMasComprada() {
        SessionFactory sf = HibernateUtils.getSessionFactory();
        try(Session session = sf.openSession()) {
            Transaction tx = session.beginTransaction();
        	Query<Object[]> query = session.createQuery(
                    "SELECT t, SUM(il.unitPrice * il.quantity) " +
                    "FROM TrackClass t LEFT JOIN InvoiceLineClass il " +
                    "ON t.trackId = il.trackId " +
                    "GROUP BY t.trackId " +
                    "HAVING SUM(il.unitPrice * il.quantity) IS NOT NULL " +
                    "ORDER BY SUM(il.quantity) DESC", Object[].class).setMaxResults(1);  
       
        	Object[] datosCancion = query.getSingleResult();
            TrackClass track = (TrackClass) datosCancion[0];
            BigDecimal facturado = (BigDecimal) datosCancion[1];

            System.out.println("ID Canción: " + track.getTrackId());
            System.out.println("Nombre: " + track.getName());
            System.out.println("Total facturado: " + facturado);
            
            tx.commit();
        } catch (Exception e){
            System.out.println("Ocurrió un error: " + e);
        }
    }
    
    public static void CancionMenosComprada() {
        SessionFactory sf = HibernateUtils.getSessionFactory();
        try(Session session = sf.openSession()) {
            Transaction tx = session.beginTransaction();
            Query<Object[]> query = session.createQuery(
                    "SELECT t, SUM(il.unitPrice * il.quantity) " +
                    "FROM TrackClass t LEFT JOIN InvoiceLineClass il " +
                    "ON t.trackId = il.trackId " +
                    "GROUP BY t.trackId " +
                    "HAVING SUM(il.unitPrice * il.quantity) IS NOT NULL " +
                    "ORDER BY SUM(il.quantity) ASC", Object[].class).setMaxResults(1);
       
        	Object[] datosCancion = query.getSingleResult();
            TrackClass track = (TrackClass) datosCancion[0];
            BigDecimal facturado = (BigDecimal) datosCancion[1];

            System.out.println("ID Canción: " + track.getTrackId());
            System.out.println("Nombre: " + track.getName());
            System.out.println("Total facturado: " + facturado);
            
            tx.commit();
        } catch (Exception e){
            System.out.println("Ocurrió un error: " + e);
        }
    }
    
    
    public static void CancionMasUsada() {
        SessionFactory sf = HibernateUtils.getSessionFactory();
        try(Session session = sf.openSession()) {
            Transaction tx = session.beginTransaction();
            Query<Object[]> query = session.createQuery(
                    "SELECT pt.trackId, t.name, COUNT(pt.trackId) AS numVeces " +
                    "FROM PlaylistTrackClass pt JOIN TrackClass t " +
                    "ON pt.trackId = t.trackId " +
                    "GROUP BY pt.trackId, t.name " +
                    "ORDER BY numVeces DESC", Object[].class).setMaxResults(1);
       
        	Object[] datosCancion = query.getSingleResult();
            Integer trackId = (Integer) datosCancion[0];
            String nombre = (String) datosCancion[1];
            Long numVeces = (Long) datosCancion[2];

            System.out.println("ID Canción: " + trackId);
            System.out.println("Nombre: " + nombre);
            System.out.println("Veces usada: " + numVeces);
            
            tx.commit();
        } catch (Exception e){
            System.out.println("Ocurrió un error: " + e);
        }
    }
    
    public static void CancionMenosUsada() {
        SessionFactory sf = HibernateUtils.getSessionFactory();
        try(Session session = sf.openSession()) {
            Transaction tx = session.beginTransaction();
            Query<Object[]> query = session.createQuery(
                    "SELECT pt.trackId, t.name, COUNT(pt.trackId) AS numVeces " +
                    "FROM PlaylistTrackClass pt JOIN TrackClass t " +
                    "ON pt.trackId = t.trackId " +
                    "GROUP BY pt.trackId, t.name " +
                    "ORDER BY numVeces ASC", Object[].class).setMaxResults(1);
       
        	Object[] datosCancion = query.getSingleResult();
            Integer trackId = (Integer) datosCancion[0];
            String nombre = (String) datosCancion[1];
            Long numVeces = (Long) datosCancion[2];

            System.out.println("ID Canción: " + trackId);
            System.out.println("Nombre: " + nombre);
            System.out.println("Veces usada: " + numVeces);
            
            tx.commit();
        } catch (Exception e){
            System.out.println("Ocurrió un error: " + e);
        }
    }
    
    public static void ClienteMenorFacturacion() {
        SessionFactory sf = HibernateUtils.getSessionFactory();
        try(Session session = sf.openSession()) {
            Transaction tx = session.beginTransaction();
            Query<Object[]> query = session.createQuery(
                    "SELECT c.customerId, c.firstName, c.lastName, c.email, SUM(i.total) AS totalFacturado " +
                    "FROM InvoiceClass i " +
                    "JOIN CustomerClass c " +
                    "ON i.customerId = c.customerId " +
                    "GROUP BY c.customerId " +
                    "ORDER BY totalFacturado DESC", Object[].class).setMaxResults(1);
       
        	Object[] datos = query.getSingleResult();
        	Integer customerId = (Integer) datos[0];
        	String nombre = (String) datos[1];
        	String apellido = (String) datos[2];
        	String correo = (String) datos[3];
        	BigDecimal totalFacturas = (BigDecimal) datos[4];
        	
        	System.out.println("ID Cliente: " + customerId);
        	System.out.println("Nombre completo: " + nombre + " " + apellido);
        	System.out.println("Correo: " + correo);
        	System.out.println("Total facturado: " + totalFacturas);
            
            tx.commit();
        } catch (Exception e){
            System.out.println("Ocurrió un error: " + e);
        }
    }
    
    public static void GeneroMusicalMasComprado() {
        SessionFactory sf = HibernateUtils.getSessionFactory();
        try(Session session = sf.openSession()) {
            Transaction tx = session.beginTransaction();
            Query<Object[]> query = session.createQuery(
            		"SELECT g.name, SUM(il.quantity) AS totalComprado " +
            		"FROM InvoiceLineClass il " +
            		"JOIN TrackClass t ON il.trackId = t.trackId " +
            		"JOIN GenreClass g ON t.genreId = g.genreId " +
            		"GROUP BY g.genreId, g.name " +
            		"ORDER BY totalComprado DESC", Object[].class).setMaxResults(1);
       
        	Object[] datos = query.getSingleResult();
        	
        	System.out.println("Nombre: " + datos[0]);
        	System.out.println("Total comprado: " + datos[1]);

            tx.commit();
        } catch (Exception e){
            System.out.println("Ocurrió un error: " + e);
        }
    }
    
    public static void GeneroMusicalMenosComprado() {
        SessionFactory sf = HibernateUtils.getSessionFactory();
        try(Session session = sf.openSession()) {
            Transaction tx = session.beginTransaction();
            Query<Object[]> query = session.createQuery(
            		"SELECT g.name, SUM(il.quantity) AS totalComprado " +
            		"FROM InvoiceLineClass il " +
            		"JOIN TrackClass t ON il.trackId = t.trackId " +
            		"JOIN GenreClass g ON t.genreId = g.genreId " +
            		"GROUP BY g.genreId, g.name " +
            		"ORDER BY totalComprado ASC", Object[].class).setMaxResults(1);
       
        	Object[] datos = query.getSingleResult();
        	
        	System.out.println("Nombre: " + datos[0]);
        	System.out.println("Total comprado: " + datos[1]);

            tx.commit();
        } catch (Exception e){
            System.out.println("Ocurrió un error: " + e);
        }
    }
  
    
    
    public static void FacturacionPorPais(String pais) {
        SessionFactory sf = HibernateUtils.getSessionFactory();
        try (Session session = sf.openSession()) {
            Transaction tx = session.beginTransaction();
            Query<Object[]> query = session.createQuery(
            		"SELECT billingCountry, SUM(total) " + 
            		"FROM InvoiceClass " + 
            		"WHERE billingCountry = :pais " + 
            		"GROUP BY billingCountry " + 
            		"ORDER BY sum(total) DESC", Object[].class).setMaxResults(1);
            query.setParameter("pais", pais);
            
            Object[] datosPais = query.getSingleResult();
            System.out.println("Pais: " + (String) datosPais[0]);
            System.out.println("Facturación: " + (BigDecimal) datosPais[1]);

            tx.commit();
        } catch (Exception e){
            System.out.println("Ocurrió un error: " + e);
        }
    }
    
    public static void CancionesMasAnadidas() {
        SessionFactory sf = HibernateUtils.getSessionFactory();
        try(Session session = sf.openSession()) {
            Transaction tx = session.beginTransaction();
            Query<Object[]> query = session.createQuery(
                    "SELECT pt.trackId, t.name, COUNT(pt.trackId) AS numVeces " +
                    "FROM PlaylistTrackClass pt JOIN TrackClass t " +
                    "ON pt.trackId = t.trackId " +
                    "GROUP BY pt.trackId, t.name " +
                    "ORDER BY numVeces DESC", Object[].class).setMaxResults(10);
       
        	List<Object[]> datosCanciones = query.list();
        	
        	for(Object[] datosCancion: datosCanciones) {
                Integer trackId = (Integer) datosCancion[0];
                String nombre = (String) datosCancion[1];
                Long numVeces = (Long) datosCancion[2];

                System.out.println("ID Canción: " + trackId);
                System.out.println("Nombre: " + nombre);
                System.out.println("Veces usada: " + numVeces + "\n");
        	}
            
            tx.commit();
        } catch (Exception e){
            System.out.println("Ocurrió un error: " + e);
        }
    }

    
//    public static void main(String[] args) {
//    	
//    }
}