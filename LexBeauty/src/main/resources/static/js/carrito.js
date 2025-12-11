function incrementQty(id, maxStock) {
    const input = document.getElementById('qty-' + id);
    let value = parseInt(input.value);
    if (value < maxStock) {
        input.value = value + 1;
    }
}

function decrementQty(id) {
    const input = document.getElementById('qty-' + id);
    let value = parseInt(input.value);
    if (value > 1) {
        input.value = value - 1;
    }
}

function agregarAlCarrito(idProducto) {
    const qty = document.getElementById('qty-' + idProducto).value;

    const params = new URLSearchParams();
    params.append('idProducto', idProducto);
    params.append('cantidad', qty);

    fetch('/carrito/agregar', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: params.toString()
    }).then(response => {
        if (response.ok) {
            alert('Producto agregado al carrito');
        } else {
            alert('Error al agregar al carrito');
        }
    });
}