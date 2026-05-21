# Tổng Hợp Review Dự Án `Android58_Day07`

## 1. Mục tiêu của dự án

Đây là một dự án Android Native viết bằng Java, dùng để minh họa nhiều cách truyền dữ liệu trong Android:

- Truyền dữ liệu giữa `Activity` và `Activity` bằng `Intent`
- Gửi object bằng `Parcelable`
- Chia sẻ dữ liệu giữa `Activity` và `Fragment` bằng `Shared ViewModel` + `LiveData`
- Gửi sự kiện bằng `EventBus`
- Gọi ngược từ `Fragment` về `Activity` bằng interface callback
- Nhận kết quả từ màn hình khác bằng `ActivityResultLauncher`
- Mở ứng dụng email bằng `Intent.ACTION_SENDTO`

Lưu ý: tên thư mục có chữ `Kotlin`, nhưng mã nguồn hiện tại là Java.

---

## 2. Cấu trúc chính của dự án

### 2.1 File code

- `app/src/main/java/com/codewithngoc/android58day07/MainActivity.java`
  - Màn hình chính.
  - Nhập `name` và `age`.
  - Tạo `UserFragment`.
  - Đẩy dữ liệu sang `Fragment` bằng `ShareViewModel`.
  - Gửi message sang `Fragment` bằng `EventBus`.

- `app/src/main/java/com/codewithngoc/android58day07/InfoMainActivity.java`
  - Màn hình hiển thị thông tin user.
  - Nhận object `User` từ `Intent`.
  - Có nút trả kết quả về `MainActivity`.
  - Có nút mở app email.

- `app/src/main/java/com/codewithngoc/android58day07/fragment/UserFragment.java`
  - Hiển thị thông tin user trong fragment.
  - Quan sát dữ liệu từ `ShareViewModel`.
  - Nhận `MessageEvent` từ `EventBus`.
  - Có callback interface để gửi dữ liệu ngược về `Activity`.

- `app/src/main/java/com/codewithngoc/android58day07/model/User.java`
  - Model dữ liệu user gồm `name`, `age`, `email`.
  - Implement `Parcelable`.

- `app/src/main/java/com/codewithngoc/android58day07/model/ShareViewModel.java`
  - Giữ `MutableLiveData<String> name`
  - Giữ `MutableLiveData<Integer> age`
  - Giữ `MutableLiveData<String> message`

- `app/src/main/java/com/codewithngoc/android58day07/model/MessageEvent.java`
  - Class event đơn giản để gửi message qua `EventBus`.

### 2.2 File giao diện

- `activity_main.xml`
  - 2 ô nhập `name`, `age`
  - 1 `FrameLayout` để chứa fragment
  - 1 nút `Save`

- `fragment_user.xml`
  - Hiển thị `name`, `age`, `email`
  - `EditText` nhận message
  - Nút `Send`

- `activity_info_main.xml`
  - Hiển thị thông tin user
  - Nút `Back`
  - Nút `Send Email`

### 2.3 Cấu hình

- `AndroidManifest.xml`
  - Khai báo `MainActivity`, `InfoMainActivity`
  - Có quyền `CAMERA`, nhưng hiện tại chưa dùng thực tế

- `app/build.gradle.kts`
  - Dùng `EventBus`
  - Dùng `appcompat`, `material`, `activity`, `constraintlayout`

---

## 3. Luồng xử lý chính của ứng dụng

## 3.1 Luồng tại `MainActivity`

Trong `MainActivity`, luồng hiện tại là:

1. Ánh xạ `editName`, `editAge`, `btnSave`
2. Tạo `ShareViewModel` dùng chung với `Fragment`
3. Đăng ký `ActivityResultLauncher`
4. Đăng ký launcher xin quyền
5. Khi bấm `Save`:
   - đọc `name`, `age`
   - validate rỗng
   - nếu fragment chưa tồn tại thì tạo fragment
   - cập nhật `shareViewModel.setName(name)`
   - cập nhật `shareViewModel.setAge(age)`
   - gửi `MessageEvent` qua `EventBus`

Code logic chính:

```java
btnSave.setOnClickListener(v -> {
    String name = editName.getText().toString().trim();
    String ageStr = editAge.getText().toString().trim();

    if (name.isEmpty()) return;
    if (ageStr.isEmpty()) return;

    UserFragment fragment = (UserFragment)
            getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

    if (fragment == null) {
        fragment = new UserFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    shareViewModel.setName(name);
    shareViewModel.setAge(Integer.parseInt(ageStr));
    EventBus.getDefault().post(new MessageEvent("Dữ liệu từ: " + name));
});
```

Ý nghĩa:

- `ViewModel` dùng để cập nhật dữ liệu có tính trạng thái
- `EventBus` dùng để phát sự kiện message
- `FragmentManager` dùng để gắn fragment động vào `FrameLayout`

## 3.2 Luồng tại `UserFragment`

`UserFragment` có 3 hướng nhận dữ liệu:

### Cách 1: Qua `Bundle`

Fragment có `newInstance(name, age, email)`:

```java
public static UserFragment newInstance(String name, int age, String email) {
    UserFragment fragment = new UserFragment();
    Bundle args = new Bundle();
    args.putString(ARG_NAME, name);
    args.putInt(ARG_AGE, age);
    args.putString(ARG_EMAIL, email);
    fragment.setArguments(args);
    return fragment;
}
```

Ý nghĩa:

- Dùng khi tạo fragment mới và muốn Android khôi phục lại được dữ liệu sau khi recreate.

### Cách 2: Qua `Shared ViewModel`

Trong `onCreateView()` fragment lấy chung ViewModel với activity:

```java
shareViewModel = new ViewModelProvider(requireActivity()).get(ShareViewModel.class);

shareViewModel.getName().observe(getViewLifecycleOwner(), name -> {
    tvName.setText("Name: " + name);
});

shareViewModel.getAge().observe(getViewLifecycleOwner(), age -> {
    tvAge.setText("Age: " + age);
});
```

Ý nghĩa:

- `Activity` set dữ liệu
- `Fragment` observe dữ liệu
- khi dữ liệu đổi, UI trong fragment đổi theo

### Cách 3: Qua `EventBus`

Fragment đăng ký EventBus ở `onStart()` và hủy ở `onStop()`:

```java
@Override
public void onStart() {
    super.onStart();
    EventBus.getDefault().register(this);
}

@Override
public void onStop() {
    super.onStop();
    EventBus.getDefault().unregister(this);
}
```

Nhận sự kiện:

```java
@Subscribe(threadMode = ThreadMode.MAIN)
public void onMessageEvent(MessageEvent event) {
    editMessage.setText(event.getMessage());
}
```

Ý nghĩa:

- `MainActivity` post event
- `UserFragment` nhận event trên main thread
- `EditText` trong fragment được cập nhật ngay

## 3.3 Luồng `InfoMainActivity`

Màn này dùng để minh họa truyền object `User` qua `Intent`.

Nhận dữ liệu:

```java
Intent intent = getIntent();
User user = intent.getParcelableExtra("USER");
```

Hiển thị:

```java
tvName.setText("Name: " + user.getName());
tvAge.setText("Age: " + user.getAge());
tvEmail.setText("Email: " + user.getEmail());
```

Trả kết quả về:

```java
Intent resultIntent = new Intent();
resultIntent.putExtra("MESSAGE", "Thông tin đã được xem");
setResult(RESULT_OK, resultIntent);
finish();
```

Mở email:

```java
Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
emailIntent.setData(Uri.parse("mailto:" + user.getEmail()));
emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello " + user.getName());
emailIntent.putExtra(Intent.EXTRA_TEXT, "Xin chào " + user.getName());
startActivity(Intent.createChooser(emailIntent, "Chọn ứng dụng gửi email"));
```

---

## 4. Ý nghĩa của từng kỹ thuật đang được dạy

## 4.1 `Parcelable`

File: `User.java`

Mục đích:

- Cho phép gửi object `User` qua `Intent`
- Nhanh hơn `Serializable` trong Android

Phần quan trọng:

```java
protected User(Parcel in) {
    name = in.readString();
    age = in.readInt();
    email = in.readString();
}

@Override
public void writeToParcel(@NonNull Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeInt(age);
    dest.writeString(email);
}
```

Nguyên tắc học:

- Ghi dữ liệu vào `Parcel` theo thứ tự nào thì đọc lại đúng thứ tự đó.

## 4.2 `Shared ViewModel`

File: `ShareViewModel.java`

Mục đích:

- Lưu trạng thái tạm thời dùng chung giữa `Activity` và `Fragment`
- Tự động cập nhật UI qua `LiveData`

Code:

```java
private final MutableLiveData<String> name = new MutableLiveData<>();
private final MutableLiveData<Integer> age = new MutableLiveData<>();
private final MutableLiveData<String> message = new MutableLiveData<>();
```

## 4.3 `ActivityResultLauncher`

Mục đích:

- Cách hiện đại để mở activity và nhận kết quả thay cho `startActivityForResult`
- Cũng dùng được cho xin quyền runtime

Trong code hiện tại, launcher đã được đăng ký nhưng chưa được dùng để mở `InfoMainActivity`.

## 4.4 `EventBus`

Mục đích:

- Gửi thông tin kiểu publish/subscribe
- Giảm phụ thuộc trực tiếp giữa bên gửi và bên nhận

Nhược điểm:

- Dễ làm code khó trace nếu lạm dụng
- Phải quản lý vòng đời `register/unregister` rất cẩn thận

## 4.5 Interface callback từ Fragment về Activity

Mục đích:

- `Fragment` gọi ngược về `Activity`
- Là cách cơ bản và rất phổ biến

Khai báo:

```java
public interface OnDataSendListener {
    void onDataSend(String name, int age, String email);
}
```

---

## 5. Review code: các vấn đề logic hiện tại

Phần này là quan trọng nhất nếu bạn dùng tài liệu này để làm bài tập hoặc trình bày.

## 5.1 Lỗi nghiêm trọng: `MainActivity` gọi `EventBus.unregister(this)` nhưng chưa từng `register(this)`

Vị trí:

- `MainActivity.java:183-187`

Phân tích:

- Trong `MainActivity`, không có đoạn nào `EventBus.getDefault().register(this)`.
- Nhưng ở `onDestroy()` lại gọi `unregister(this)`.
- Với EventBus, unregister một object chưa register có thể gây exception.

Kết luận:

- Đây là lỗi vòng đời cần sửa ngay.

## 5.2 Callback từ `Fragment` về `Activity` chưa hoạt động

Vị trí:

- `UserFragment.java:65`
- `UserFragment.java:121-127`
- `UserFragment.java:191-193`
- `MainActivity.java:27`

Phân tích:

- `MainActivity` có `implements UserFragment.OnDataSendListener`.
- `UserFragment` có `setOnDataSendListener(...)`.
- Nhưng `MainActivity` không hề gọi `fragment.setOnDataSendListener(this)`.

Hậu quả:

- Bấm nút `Send` trong fragment sẽ không gọi được `onDataSend(...)`.
- `dataSendListener` luôn là `null`.

Kết luận:

- Cơ chế callback mới khai báo, chưa nối dây đầy đủ.

## 5.3 `UserFragment` đọc dữ liệu từ `Bundle`, nhưng `MainActivity` lại không dùng `newInstance(...)`

Vị trí:

- `UserFragment.java:74-81`
- `UserFragment.java:113-118`
- `MainActivity.java:149`

Phân tích:

- Fragment có hỗ trợ `newInstance(name, age, email)`.
- Nhưng `MainActivity` lại tạo fragment bằng `new UserFragment()`.
- Vì vậy `getArguments()` thường là `null`.

Hậu quả:

- Dữ liệu `name`, `age`, `email` trong `Bundle` không được truyền vào fragment.
- Nút `Send` trong fragment nếu có callback thì cũng chỉ gửi `""`, `0`, `""`.

Kết luận:

- Dự án đang trộn nhiều cách truyền dữ liệu, nhưng chưa hoàn thiện từng cách đến nơi đến chốn.

## 5.4 `InfoMainActivity` đã khai báo đầy đủ nhưng chưa có luồng nào mở nó

Vị trí:

- `InfoMainActivity.java`
- `MainActivity.java:66-78`

Phân tích:

- `launcher` được đăng ký để nhận kết quả từ activity khác.
- Nhưng trong `MainActivity` hiện không có đoạn tạo `Intent`, gắn `User`, rồi gọi `launcher.launch(intent)`.

Hậu quả:

- `InfoMainActivity` tồn tại nhưng đang là ví dụ chưa gắn vào luồng thực thi.

## 5.5 `permissionLauncher` được khai báo nhưng chưa xin quyền thật

Vị trí:

- `MainActivity.java:80-90`
- `AndroidManifest.xml`

Phân tích:

- App đã khai báo `CAMERA` permission trong manifest.
- Nhưng code không gọi `permissionLauncher.launch(Manifest.permission.CAMERA)`.

Kết luận:

- Phần xin quyền mới dừng ở mức demo API, chưa có thao tác thật.

## 5.6 `ShareViewModel.message` đang được observe nhưng nhánh code set dữ liệu đã bị comment

Vị trí:

- `ShareViewModel.java`
- `MainActivity.java:58-60`
- `MainActivity.java:95-127`
- `UserFragment.java:143-147`

Phân tích:

- `message` có observer ở `MainActivity` và `UserFragment`.
- Nhưng đoạn `shareViewModel.setMessage(...)` đang nằm trong block comment.
- Luồng đang chạy thực tế dùng `EventBus` để cập nhật `editMessage`, không dùng `message` của `ViewModel`.

Kết luận:

- Có 2 hướng làm trùng nhau, dễ gây rối khi học bài.

## 5.7 Có import hoặc method chưa dùng

Vị trí:

- `MainActivity.java:21` import `User`
- `MainActivity.java:178-180` method `sendMessage`

Phân tích:

- `User` chưa được dùng trong `MainActivity`
- `sendMessage()` cũng không được gọi

Kết luận:

- Đây là dấu hiệu code demo đang dở dang hoặc đã đổi hướng mà chưa dọn lại.

## 5.8 Bộ test hiện tại chỉ là test mặc định

Vị trí:

- `app/src/test/.../ExampleUnitTest.java`
- `app/src/androidTest/.../ExampleInstrumentedTest.java`

Kết luận:

- Chưa có test cho logic truyền dữ liệu, callback, lifecycle hay intent.

---

## 6. Đánh giá tổng thể

### Điểm tốt

- Dự án gom được nhiều kỹ thuật Android quan trọng trong cùng một bài.
- Cách đặt tên class, layout và model khá dễ hiểu.
- `User` dùng `Parcelable` đúng hướng học Android.
- `Shared ViewModel` và `LiveData` được áp dụng đúng ý tưởng cơ bản.

### Điểm yếu

- Đang trộn quá nhiều cơ chế truyền dữ liệu trong một màn mà chưa phân tách rõ vai trò.
- Có nhiều phần demo chưa nối luồng hoàn chỉnh.
- Có lỗi vòng đời với `EventBus`.
- Callback `Fragment -> Activity` chưa được cấu hình xong.
- `InfoMainActivity` chưa được gọi thực tế.

### Kết luận ngắn

Đây là một dự án học tập tốt để hiểu nhiều kỹ thuật Android, nhưng chưa phải một flow hoàn chỉnh để đưa vào production. Nếu dùng để làm bài tập, bạn nên trình bày theo hướng:

- đây là project demo các cách truyền dữ liệu
- đã minh họa được nhiều kỹ thuật
- nhưng còn tồn tại một số lỗi logic và phần chưa hoàn thiện

---

## 7. Cách trình bày logic dự án khi làm bài

Bạn có thể trình bày ngắn gọn theo mẫu sau:

### 7.1 Bài toán

Ứng dụng cho phép nhập thông tin người dùng ở `MainActivity`, hiển thị lại trong `UserFragment`, đồng thời minh họa nhiều cơ chế truyền dữ liệu trong Android.

### 7.2 Giải pháp

- Dùng `ViewModel` để chia sẻ `name`, `age` giữa `Activity` và `Fragment`
- Dùng `EventBus` để gửi message text sang fragment
- Dùng `Parcelable` để gửi object `User` giữa hai activity
- Dùng `ActivityResultLauncher` để nhận dữ liệu trả về
- Dùng callback interface để fragment có thể gửi dữ liệu ngược về activity

### 7.3 Luồng chạy

1. Người dùng nhập `name`, `age`
2. Bấm `Save`
3. `MainActivity` tạo `UserFragment` nếu chưa có
4. `MainActivity` cập nhật `ShareViewModel`
5. `UserFragment` observe `LiveData` và cập nhật UI
6. `MainActivity` gửi `MessageEvent`
7. `UserFragment` nhận event và hiển thị message

### 7.4 Kiến thức rút ra

- Khi truyền object giữa activity nên ưu tiên `Parcelable`
- Khi chia sẻ dữ liệu UI giữa `Activity` và `Fragment` nên dùng `Shared ViewModel`
- `EventBus` chỉ nên dùng khi thực sự cần event rời rạc
- `Fragment` callback là cách đơn giản để giao tiếp ngược về `Activity`
- Phải quản lý lifecycle cẩn thận khi `register/unregister`

---

## 8. Gợi ý cải tiến nếu muốn hoàn thiện bài

Nếu muốn biến project này thành bài tập hoàn chỉnh hơn, nên làm theo thứ tự:

1. Sửa lỗi `EventBus.unregister(this)` trong `MainActivity`
2. Khi tạo fragment, dùng `UserFragment.newInstance(...)` hoặc chỉ dùng hẳn `ViewModel`, không trộn cả hai nếu không cần
3. Gọi `fragment.setOnDataSendListener(this)` để callback chạy được
4. Tạo object `User`, mở `InfoMainActivity` bằng `launcher.launch(intent)`
5. Nếu giữ phần xin quyền camera thì thêm nút xin quyền và xử lý thực tế
6. Viết thêm test hoặc ít nhất test tay theo các luồng chính

---

## 9. Tóm tắt học nhanh

### Dự án này đang dạy gì?

- `Intent`
- `Parcelable`
- `Fragment`
- `Shared ViewModel`
- `LiveData`
- `EventBus`
- `ActivityResultLauncher`
- `Intent mở email`

### Dự án này đang thiếu gì?

- Luồng mở `InfoMainActivity`
- Gắn callback `Fragment -> Activity`
- Xin quyền camera thực tế
- Test logic
- Đồng bộ sạch giữa các cách truyền dữ liệu

### Nhận xét cuối

Nếu bạn dùng tài liệu này để làm bài tập, cách nói tốt nhất là:

> Dự án là bài demo tổng hợp nhiều cơ chế giao tiếp trong Android. Phần mạnh là minh họa được nhiều kỹ thuật trong cùng một app; phần yếu là một số flow còn dở dang và có lỗi lifecycle cần chỉnh lại để chạy ổn định.

