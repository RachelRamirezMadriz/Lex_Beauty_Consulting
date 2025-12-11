// Producttos

function incrementQty(id, maxStock) {
    const input = document.getElementById('qty-' + id);
    let value = parseInt(input.value);
    if (value < maxStock) input.value = value + 1;
}

function decrementQty(id) {
    const input = document.getElementById('qty-' + id);
    let value = parseInt(input.value);
    if (value > 1) input.value = value - 1;
}

function agregarAlCarrito(idProducto, maxStock) {
    const input = document.getElementById('qty-' + idProducto);
    let qty = parseInt(input.value);

    const params = new URLSearchParams();
    params.append('idProducto', idProducto);
    params.append('cantidad', qty);

    fetch('/carrito/agregar', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: params.toString()
    }).then(response => {
        if (response.ok) {
            // Actualizar stock visual en la card
            maxStock -= qty;
            if (maxStock <= 0) {
                document.getElementById('btn-inc-' + idProducto).disabled = true;
                document.getElementById('btn-carrito-' + idProducto).disabled = true;
                const card = document.getElementById('qty-' + idProducto).closest('.card');
                const badge = document.createElement('div');
                badge.className = 'position-absolute top-0 start-0 m-2 p-1 bg-danger text-white fw-bold rounded';
                badge.innerText = 'No disponible';
                card.appendChild(badge);
            }
            // Reiniciar cantidad a 1
            input.value = 1;
        } else {
            alert('Error al agregar al carrito');
        }
    });
}

// Carrito

function incrementQtyCarrito(detalleId, stock) {
    const input = document.getElementById('qty-cart-' + detalleId);
    let cantidad = parseInt(input.value);
    if (cantidad < stock) {
        cantidad++;
        input.value = cantidad;
        actualizarCantidad(detalleId, cantidad);
    }
}

function decrementQtyCarrito(detalleId) {
    const input = document.getElementById('qty-cart-' + detalleId);
    let cantidad = parseInt(input.value);
    if (cantidad > 1) {
        cantidad--;
        input.value = cantidad;
        actualizarCantidad(detalleId, cantidad);
    }
}

function actualizarCantidad(detalleId, cantidad) {
    const params = new URLSearchParams();
    params.append('detalleId', detalleId);
    params.append('cantidad', cantidad);

    fetch('/carrito/actualizar', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: params.toString()
    }).then(response => {
        if(response.ok){
            location.reload(); l
        } else {
            alert('Error al actualizar la cantidad');
        }
    });
}


