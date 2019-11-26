# Cartelera
-[OK]MainActivity -> ListView
-[OK]VerPeli -> nombre, sinop...
-[OK]Peli -> Atributos:
  -int ID
  -String nombre, sinopsis
  -Date fecha_salida
  -String reparto
  -int duracion
  Constructores todos + vacio
  Getter + setter
-[OK]Database ->
  -onCreate(): var tipo text, ID y duracion INTEGER
  -insertarFila(Peli peli)
  -eliminarFila(Peli peli) where ID = peli.ID
  -getFilas() return ArrayList<Peli>
  -class Constantes
-[OK]InsertarPeli
