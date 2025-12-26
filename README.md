# ThanhMP3
ThanhMP3

//Create
MusicModel music = new MusicModel(
3,
"New Song",
"https://img.jpg",
"Artist",
"Rap",
"https://song.mp3"
);

musicRef.push().setValue(music)
.addOnSuccessListener(unused -> Log.d("Firebase", "Create success"))
.addOnFailureListener(e -> Log.e("Firebase", "Create failed", e));

//delete
int targetId = 1;

Query query = FirebaseDatabase.getInstance()
.getReference("list_music")
.orderByChild("id")
.equalTo(targetId);

query.addListenerForSingleValueEvent(new ValueEventListener() {
@Override
public void onDataChange(@NonNull DataSnapshot snapshot) {

        for (DataSnapshot child : snapshot.getChildren()) {
            child.getRef().removeValue(); // 🔥 DELETE
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {}
});


//update
int targetId = 1;

Query query = FirebaseDatabase.getInstance()
.getReference("list_music")
.orderByChild("id")
.equalTo(targetId);

query.addListenerForSingleValueEvent(new ValueEventListener() {
@Override
public void onDataChange(@NonNull DataSnapshot snapshot) {

        for (DataSnapshot child : snapshot.getChildren()) {
            String key = child.getKey(); // 🔥 KEY FIREBASE

            // UPDATE
            child.getRef().child("name").setValue("Tên mới");
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {}
});


//Lấy KEY Firebase để UPDATE / DELETE
for (DataSnapshot child : snapshot.getChildren()) {
MusicModel music = child.getValue(MusicModel.class);
String key = child.getKey(); // 🔥 KEY FIREBASE
}
