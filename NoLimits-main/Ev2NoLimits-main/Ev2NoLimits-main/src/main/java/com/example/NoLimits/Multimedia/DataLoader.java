    package com.example.NoLimits.Multimedia;

    import java.util.List;
    import java.util.Random;
    import java.time.LocalDate;
    import java.time.LocalTime;
    import java.time.ZoneId;
    import java.util.Date;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.CommandLineRunner;
    import org.springframework.context.annotation.Profile;
    import org.springframework.stereotype.Component;

    import com.example.NoLimits.Multimedia.model.AccesorioModel;
    import com.example.NoLimits.Multimedia.model.DetallePedidoModel;
    import com.example.NoLimits.Multimedia.model.DetalleVentaModel;
    import com.example.NoLimits.Multimedia.model.MetodoPagoModel;
    import com.example.NoLimits.Multimedia.model.PedidoModel;
    import com.example.NoLimits.Multimedia.model.PeliculaModel;
    import com.example.NoLimits.Multimedia.model.TipoProductoModel;
    import com.example.NoLimits.Multimedia.model.UsuarioModel;
    import com.example.NoLimits.Multimedia.model.VentasModel;
    import com.example.NoLimits.Multimedia.model.VideojuegoModel;
    import com.example.NoLimits.Multimedia.repository.AccesorioRepository;
    import com.example.NoLimits.Multimedia.repository.DetallePedidoRepository;
    import com.example.NoLimits.Multimedia.repository.DetalleVentaRepository;
    import com.example.NoLimits.Multimedia.repository.MetodoPagoRepository;
    import com.example.NoLimits.Multimedia.repository.PedidoRepository;
    import com.example.NoLimits.Multimedia.repository.PeliculaRepository;
    import com.example.NoLimits.Multimedia.repository.TipoProductoRepository;
    import com.example.NoLimits.Multimedia.repository.UsuarioRepository;
    import com.example.NoLimits.Multimedia.repository.VentasRepository;
    import com.example.NoLimits.Multimedia.repository.VideojuegoRepository;

    import net.datafaker.Faker;

    // Se usa para activar y desactivar Beans dependiendo del entorno.
    // Bean: Objeto que se está gestionando en el controlador de Spring.
    @Profile("dev")
    // Marca una clase como un Bean genérico.
    @Component
    public class DataLoader implements CommandLineRunner{

        @Autowired
        private AccesorioRepository accesorioRepository;

        @Autowired
        private DetallePedidoRepository detallePedidoRepository;

        @Autowired
        private DetalleVentaRepository detalleVentaRepository;

        @Autowired
        private MetodoPagoRepository metodoPagoRepository;

        @Autowired
        private PedidoRepository pedidoRepository;

        @Autowired
        private PeliculaRepository peliculaRepository;

        @Autowired
        private TipoProductoRepository tipoProductoRepository;

        @Autowired
        private UsuarioRepository usuarioRepository;

        @Autowired
        private VentasRepository ventasRepository;

        @Autowired
        private VideojuegoRepository videojuegoRepository;

        @Override
        public void run(String... args) throws Exception {

            Faker faker = new Faker();
            Random random = new Random();

            // TIPO PRODUCTO
            for (int i = 0; i < 5; i++) {
                String nombreTipo = faker.commerce().department();
                if (!tipoProductoRepository.existsByNombreTipoProducto(nombreTipo)) {
                    TipoProductoModel tipoProducto = new TipoProductoModel();
                    tipoProducto.setNombreTipoProducto(nombreTipo);
                    tipoProductoRepository.save(tipoProducto);
                }
            }

            // Asegura que exista "Sports"
            if (!tipoProductoRepository.existsByNombreTipoProducto("Sports")) {
                TipoProductoModel sportsTipo = new TipoProductoModel();
                sportsTipo.setNombreTipoProducto("Sports");
                tipoProductoRepository.save(sportsTipo);
            }

            // Se obtiene la lista de tipos de producto
            List<TipoProductoModel> tiposProducto = tipoProductoRepository.findAll();

            // PELÍCULA
            for (int i = 0; i < 10; i++) {
                PeliculaModel pelicula = new PeliculaModel();
                pelicula.setNombrePelicula(faker.book().title());
                pelicula.setCategoriaPelicula(faker.book().genre());
                pelicula.setPlataformaPelicula(faker.company().name());
                pelicula.setDuracionPelicula(faker.number().numberBetween(80, 100));
                pelicula.setDescripcionPelicula(faker.lorem().sentence(15));
                pelicula.setPrecioPelicula(Float.parseFloat(faker.commerce().price(1000.0, 10000.0)));

                // Relación con tipo de producto (elegir uno aleatorio)
                if (!tiposProducto.isEmpty()) {
                    pelicula.setTipoProducto(tiposProducto.get(random.nextInt(tiposProducto.size())));
                }

                peliculaRepository.save(pelicula);
            }

            // VIDEOJUEGO
            for (int i = 0; i < 20; i++) {
                VideojuegoModel videojuego = new VideojuegoModel();
                videojuego.setNombreVideojuego(faker.videoGame().title());
                videojuego.setCategoriaVideojuego(faker.videoGame().genre());
                videojuego.setPlataformaVideojuego(faker.videoGame().platform());
                videojuego.setDesarrolladorVideojuego(faker.company().name());
                videojuego.setDescripcionVideojuego(faker.lorem().sentence(25));
                videojuego.setPrecioVideojuego((float) faker.number().randomDouble(2, 10, 100));

                //  Asignar tipo de producto aleatorio
                if (!tiposProducto.isEmpty()) {
                    videojuego.setTipoProducto(tiposProducto.get(random.nextInt(tiposProducto.size())));
                }

                videojuegoRepository.save(videojuego);
            }
            
            List<PeliculaModel> peliculas = peliculaRepository.findAll();
            List<VideojuegoModel> videojuegos = videojuegoRepository.findAll();

            // ACCESORIO
            for (int i = 0; i < 10; i++) {
                AccesorioModel accesorio = new AccesorioModel();
                accesorio.setNombreAccesorio(faker.commerce().productName()); // nombre de producto
                accesorio.setTipoAccesorio(faker.commerce().department()); // tipo/categoria
                accesorio.setMarcaAccesorio(faker.company().name()); // marca
                accesorio.setDescripcionAccesorio(faker.lorem().sentence());// descripcion breve
                accesorio.setPrecioAccesorio(Float.parseFloat(faker.commerce().price())); // precio aleatorio
                accesorio.setStockAccesorio(faker.number().numberBetween(1, 100)); // cantidad entre 1 y 1007

                // Verifica que exista la película.
                if (!peliculas.isEmpty()) {
                    accesorio.setPelicula(peliculas.get(random.nextInt(peliculas.size())));
                }
                // Verifica que exista el videojuego.
                if (!videojuegos.isEmpty()) {
                    accesorio.setVideojuego(videojuegos.get(random.nextInt(videojuegos.size())));
                }
                // Verifica que esté asociado a un tipo de producto.
                if (!tiposProducto.isEmpty()) {
                    accesorio.setTipoProducto(tiposProducto.get(random.nextInt(tiposProducto.size())));
                }

                accesorioRepository.save(accesorio);
            }
            List<AccesorioModel> accesorios = accesorioRepository.findAll();

            

            
            
            // USUARIO
            for (int i = 0; i < 50; i++) {
                String correo = faker.internet().emailAddress();
                if (!usuarioRepository.existsByCorreo(correo)) {
                    UsuarioModel usuario = new UsuarioModel();
                    usuario.setNombre(faker.name().firstName());
                    usuario.setApellidos(faker.name().lastName());
                    usuario.setCorreo(correo);
                    usuario.setTelefono(faker.number().numberBetween(10000000, 99999999));
                    usuario.setPassword(faker.internet().password(8, 10));
                    usuarioRepository.save(usuario);
                }
            }
            List<UsuarioModel> usuarios = usuarioRepository.findAll();

            if (usuarios.isEmpty()) {
                System.out.println("No hay usuarios disponibles para crear pedidos.");
                return;
            }

            // PEDIDO
            // Lista de posibles estados para los pedidos
            String[] estados = { "En proceso", "Enviado", "Entregado" };

            // Verificamos si hay usuarios disponibles para asignar a los pedidos
            if (usuarios.isEmpty()) {
                System.out.println("No hay usuarios disponibles para crear pedidos.");
                return;
            }

            // MÉTODO PAGO
            for (int i = 0; i < 5; i++) {
                String nombreMetodo = faker.finance().creditCard();
                if (!metodoPagoRepository.existsByNombre(nombreMetodo)) {
                    MetodoPagoModel metodoPago = new MetodoPagoModel();
                    metodoPago.setNombre(nombreMetodo);
                    metodoPagoRepository.save(metodoPago);
                }
            }
            // Se obtiene la lista de metodos de pago
            List<MetodoPagoModel> listaMetodosPago = metodoPagoRepository.findAll();

            // Se crean 7 pedidos con datos falsos generados por Faker
            for (int i = 0; i < 7; i++) {
                PedidoModel pedido = new PedidoModel();

                // Dirección de entrega generada aleatoriamente
                pedido.setDireccionEntrega(faker.address().fullAddress());

                // Estado aleatorio seleccionado desde el arreglo de estados
                pedido.setEstado(estados[faker.random().nextInt(estados.length)]);

                // Usuario asignado de forma aleatoria desde la lista de usuarios existentes
                UsuarioModel usuario = usuarios.get(random.nextInt(usuarios.size()));
                System.out.println("Usuario asignado al pedido: " + usuario.getId());
                pedido.setUsuarioModel(usuario);
                // Método de pago aleatorio
                pedido.setMetodoPagoModel(listaMetodosPago.get(random.nextInt(listaMetodosPago.size())));

               try {
                // Se guarda el pedido en la base de datos
                PedidoModel pedidoGuardado = pedidoRepository.save(pedido);

                // Se fuerza el guardado inmediato en la base de datos para asegurar la persistencia
                pedidoRepository.flush();

                // Se verifica que el pedido se haya guardado correctamente y que tenga un ID asignado
                if (pedidoGuardado == null || pedidoGuardado.getId() == null) {
                    System.out.println("No se pudo guardar el pedido correctamente.");
                    continue; // Se salta a la siguiente iteración si no se pudo guardar
                }
            } catch (Exception e) {
                // Captura cualquier excepción durante el proceso de guardado
                System.out.println("Error al guardar el pedido: " + e.getMessage());
                e.printStackTrace(); // Imprime la traza del error para facilitar la depuración
            }
        }

            // Se obtiene la lista actual de pedidos registrados
            List<PedidoModel> pedidos = pedidoRepository.findAll();

            // DETALLE PEDIDO
            // Se generan 15 pedidos nuevos. Por cada uno, se guardan entre 1 y 10 detalles
            // Cada detalle puede estar vinculado a una Película, Videojuego o Accesorio.
            
            // lista de entidades para las relaciones
            // List<UsuarioModel> usuarios = usuarioRepository.findAll();
            // List<PeliculaModel> peliculas = peliculaRepository.findAll();
            // List<VideojuegoModel> videojuegos = videojuegoRepository.findAll();
            // List<AccesorioModel> accesorios = accesorioRepository.findAll();

            for (int i = 0; i < 15; i++) {
                PedidoModel pedido = new PedidoModel();

                // Usuario aleatorio
                UsuarioModel usuarioAleatorio = usuarios.get(random.nextInt(usuarios.size()));
                pedido.setUsuarioModel(usuarioAleatorio);

                // direccion
                pedido.setDireccionEntrega(faker.address().fullAddress());

                // Estado aleatorio
                pedido.setEstado(faker.options().option("Pendiente", "Enviado", "Entregado", "Cancelado"));

                // Método de pago aleatorio
                pedido.setMetodoPagoModel(listaMetodosPago.get(random.nextInt(listaMetodosPago.size())));

                // Guardar el pedido por su ID y poder asociar los detalles
                PedidoModel pedidoGuardado = pedidoRepository.save(pedido);
                pedidoRepository.flush(); // Esto asegura que el pedido ya esté persistido en DB

                if (pedidoGuardado.getId() == null) {
                        System.out.println("No se pudo guardar el pedido correctamente.");
                        continue;
                }

                // Crear los detalles del pedido recien creado
                int numeroDetalles = random.nextInt(1, 10);

                for (int j = 0; j < numeroDetalles; j++) {
                    DetallePedidoModel detallePedido = new DetallePedidoModel(); // Crear un nuevo detalle de pedido
                    detallePedido.setPedido(pedidoGuardado); // Asociar el pedido al detalle

                    int cantidad = faker.number().numberBetween(1, 10); // cantidad aleatoria entre 1 y 10
                    float precioUnitario; // Variable para almacenar el precio unitario del producto

                    // Elegir un tipo de producto aleatorio
                    int tipoProductoAleatorio = random.nextInt(3); // 0: Pelicula, 1: Videojuego, 2: Accesorio

                    if (tipoProductoAleatorio == 0 && !peliculas.isEmpty()) { // Si es Pelicula
                        PeliculaModel pelicula = peliculas.get(random.nextInt(peliculas.size())); // Selecciona una pelicula aleatoria
                        detallePedido.setPelicula(pelicula); // Asociar la pelicula al detalle
                        precioUnitario = pelicula.getPrecioPelicula();
                    } else if (tipoProductoAleatorio == 1 && !videojuegos.isEmpty()) { // Si es Videojuego
                        VideojuegoModel videojuegoSeleccionado = videojuegos.get(random.nextInt(videojuegos.size())); // Selecciona un videojuego aleatorio
                        detallePedido.setVideojuego(videojuegoSeleccionado); // Asociar el videojuego al detalle
                        precioUnitario = videojuegoSeleccionado.getPrecioVideojuego();
                    } else if (!accesorios.isEmpty()) { // Si no es Pelicula ni Videojuego, se asume que es un Accesorio
                        AccesorioModel accesorioModel = accesorios.get(random.nextInt(accesorios.size())); // Selecciona un accesorio aleatorio
                        detallePedido.setAccesorio(accesorioModel); // Asociar el accesorio al detalle
                        precioUnitario = accesorioModel.getPrecioAccesorio();
                    } else { // Si no hay productos de un tipo, se salta
                        continue; // Si no hay productos de un tipo, se salta
                    }

                    detallePedido.setCantidad(cantidad); // Establecer cantidad
                    detallePedido.setPrecioUnitario(precioUnitario); // Establecer precio unitario
                    detallePedido.setSubtotal(cantidad * precioUnitario); // Calcular subtotal
                    detallePedidoRepository.save(detallePedido); // Guardar el detalle del pedido
                }
            }

            
        

            // VENTA (solo si hay métodos de pago y usuarios disponibles)
            if (!listaMetodosPago.isEmpty() && !usuarios.isEmpty()) {
                for (int i = 0; i < 5; i++) {
                    VentasModel ventas = new VentasModel();

                    // Fecha aleatoria (entre hace 1 año y hoy)
                    Date fecha = faker.date().past(365, java.util.concurrent.TimeUnit.DAYS);
                    LocalDate fechaLocal = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    ventas.setFechaCompra(fechaLocal);

                    // Hora aleatoria (solo la hora)
                    Date hora = faker.date().past(1, java.util.concurrent.TimeUnit.DAYS);
                    LocalTime horaLocal = hora.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                    ventas.setHoraCompra(horaLocal);

                    // Total aleatorio entre 10.000 y 100.000
                    ventas.setTotalVenta(faker.number().numberBetween(10000, 100000));

                    // Método de pago aleatorio
                    MetodoPagoModel metodoPagoSeleccionado = listaMetodosPago.get(random.nextInt(listaMetodosPago.size()));
                    ventas.setMetodoPagoModel(metodoPagoSeleccionado);

                    // Usuario aleatorio
                    UsuarioModel usuario = usuarios.get(random.nextInt(usuarios.size()));
                    ventas.setUsuarioModel(usuario);

                    // Guardar venta
                    ventasRepository.save(ventas);
                }
            } else {
                System.out.println("No se generaron ventas porque no hay métodos de pago o usuarios cargados.");
            }
        List<VentasModel> listaVentas = ventasRepository.findAll();

            // DETALLE VENTA
            List<VentasModel> ventasModel = ventasRepository.findAll();
            List<AccesorioModel> listaAccesorios = accesorioRepository.findAll();
            List<PeliculaModel> listaPeliculas = peliculaRepository.findAll();
            List<VideojuegoModel> listaVideojuegos = videojuegoRepository.findAll();

            if (!ventasModel.isEmpty()) {
                for (int i = 0; i < 5; i++) {
                    DetalleVentaModel detalleVenta = new DetalleVentaModel();

                    // Cantidad aleatoria entre 1 y 5
                    detalleVenta.setCantidad(faker.number().numberBetween(1, 6));

                    // Precio unitario aleatorio entre 10.000 y 50.000
                    detalleVenta.setPrecioUnitario((float) faker.number().randomDouble(0, 10000, 50000));
                    detalleVenta.setSubtotal(detalleVenta.getCantidad() * detalleVenta.getPrecioUnitario());

                    // Relación con venta
                    VentasModel venta = ventasModel.get(random.nextInt(ventasModel.size()));
                    detalleVenta.setVenta(venta);

                    // Relación con un solo producto: accesorio, película o videojuego
                    int tipoProducto = random.nextInt(3); // 0 = accesorio, 1 = película, 2 = videojuego

                    if (tipoProducto == 0 && !listaAccesorios.isEmpty()) {
                        AccesorioModel accesorio = listaAccesorios.get(random.nextInt(listaAccesorios.size()));
                        detalleVenta.setAccesorio(accesorio);
                    } else if (tipoProducto == 1 && !listaPeliculas.isEmpty()) {
                        PeliculaModel pelicula = listaPeliculas.get(random.nextInt(listaPeliculas.size()));
                        detalleVenta.setPelicula(pelicula);
                    } else if (tipoProducto == 2 && !listaVideojuegos.isEmpty()) {
                        VideojuegoModel videojuego = listaVideojuegos.get(random.nextInt(listaVideojuegos.size()));
                        detalleVenta.setVideojuego(videojuego);
                    }

                    // Solo guardamos si tiene al menos un producto asociado
                    if (detalleVenta.getAccesorio() != null || detalleVenta.getPelicula() != null || detalleVenta.getVideojuego() != null) {
                        detalleVentaRepository.save(detalleVenta);
                    }
                }
            }
        }
    }
        
