SET NOCOUNT ON;
SET XACT_ABORT ON;

BEGIN TRANSACTION;

DECLARE @categoryId INT;
SELECT TOP 1 @categoryId = CategoryId
FROM categories
WHERE LOWER(CategoryName) IN (N'laptop hp', N'laptop')
ORDER BY CategoryId;

IF @categoryId IS NULL
BEGIN
    INSERT INTO categories (CategoryName, Images, Status)
    VALUES (N'Laptop', NULL, 1);
    SET @categoryId = SCOPE_IDENTITY();
END
ELSE
BEGIN
    UPDATE categories
    SET CategoryName = N'Laptop', Status = 1
    WHERE CategoryId = @categoryId;
END;

IF NOT EXISTS (SELECT 1 FROM users WHERE Username = N'admin')
BEGIN
    INSERT INTO users
        (Username, PasswordHash, FullName, Email, CreatedAt, Active, Role, OtpAttempts)
    VALUES
        (N'admin', N'123456', N'Quan tri vien', N'admin@baitap.local',
         SYSDATETIME(), 1, N'ADMIN', 0);
END;

DELETE FROM products;

DECLARE @baseTime DATETIME2 = SYSDATETIME();

INSERT INTO products
    (ProductName, Description, Image, Price, Stock, Status, CreatedAt, CategoryId)
VALUES
    (N'Laptop HP ProBook 450 G10 B73TPAT (I5-1334U/ 16GB/ 512GB/ 15.6" FHD/ Win 11)',
     N'HP ProBook 450 G10, laptop van phong 15.6 inch, Intel Core i5, RAM 16GB, SSD 512GB.',
     N'https://cdn.hstatic.net/products/200000722513/aptop-hp-probook-450-g10-b73tpat-i5-1334u-16gb-512gb-15-6-fhd-win-11-1_92096ab979d145fb871c25cf43c69a09.jpg',
     23990000, 8, 1, DATEADD(SECOND, -9, @baseTime), @categoryId),
    (N'Laptop HP 15-fd1289TU C2CV8PA (Ultra 7-155H/ 16GB/ 512GB/ 15.6" FHD IPS/ Win 11)',
     N'HP 15-fd1289TU, man hinh FHD IPS, Intel Core Ultra 7, RAM 16GB, SSD 512GB.',
     N'https://cdn.hstatic.net/products/200000722513/p-hp-15-fd1289tu-c2cv8pa-ultra-7-155h-16gb-512gb-15-6-fhd-ips-win-11-1_06cba3ca8d234beab61e5eba8586d8b9.jpg',
     24990000, 6, 1, DATEADD(SECOND, -8, @baseTime), @categoryId),
    (N'Laptop HP 250 G10 A06FDPT (I7-1355U/ 8GB/ 512GB/ 15.6" FHD/ Win 11)',
     N'HP 250 G10, laptop hoc tap va van phong, Intel Core i7, RAM 8GB, SSD 512GB.',
     N'https://cdn.hstatic.net/products/200000722513/laptop-hp-250-g10-a06fdpt-i7-1355u-8gb-512gb-15-6-fhd-win-11-1_fc7c6c8a93d64a8da6b5e323aaa9af7a.jpg',
     23490000, 10, 1, DATEADD(SECOND, -7, @baseTime), @categoryId),
    (N'Laptop Dell Vostro 3530 2H1TPI7 (I7-1355U/ 8GB/ 512GB/ 15.6" FHD/DOS)',
     N'Dell Vostro 3530, thiet ke chac chan cho cong viec van phong, Intel Core i7, SSD 512GB.',
     N'https://cdn.hstatic.net/products/200000722513/laptop-dell-vostro-3530-2h1tpi7-i7-1355u-8gb-512gb-15-6-fhd-1_e6a713d8fbda4916b585f89c5b20c3e4.jpg',
     18990000, 7, 1, DATEADD(SECOND, -6, @baseTime), @categoryId),
    (N'Laptop Dell DC15250-5434BLK M4CFY (I5-1334U/ 8GB/ 512GB/ 15.6" FHD Touch/ Win 11)',
     N'Dell DC15250, man hinh cam ung FHD 15.6 inch, Intel Core i5, RAM 8GB, SSD 512GB.',
     N'https://cdn.hstatic.net/products/200000722513/-dell-dc15250-5434blk-m4cfy-i5-1334u-8gb-512gb-15-6-fhd-touch-win-11-1_54641088f7e04b7983947dc0cf753df9.jpg',
     17190000, 9, 1, DATEADD(SECOND, -5, @baseTime), @categoryId),
    (N'Laptop Dell 16 DC16251 LDC16251-7537CLD-PUS (Core 7-150U/ 16GB/ 1TB/ 16" FHD+ Touch/ Win 11)',
     N'Dell 16 DC16251, man hinh FHD+ cam ung, Core 7, RAM 16GB va SSD 1TB.',
     N'https://cdn.hstatic.net/products/200000722513/c16251-ldc16251-7537cld-pus-core-7-150u-16gb-1tb-16-fhd-touch-win-11-3_fa2fbb18f3ed48ffaafa065eafd86de6.jpg',
     26990000, 5, 1, DATEADD(SECOND, -4, @baseTime), @categoryId),
    (N'Laptop Dell DC15250-7982BLK H5YXJ (I7-1355U/ 16GB/ 1TB/ 15.6" FHD Touch/ Win 11)',
     N'Dell DC15250, man hinh FHD cam ung, Intel Core i7, RAM 16GB, SSD 1TB.',
     N'https://cdn.hstatic.net/products/200000722513/p-dell-dc15250-7982blk-h5yxj-i7-1355u-16gb-1tb-15-6-fhd-touch-win-11-1_d44530b4a08b468590481c774a0decae.jpg',
     22990000, 5, 1, DATEADD(SECOND, -3, @baseTime), @categoryId),
    (N'Laptop Lenovo IdeaPad Slim 3 14IWC11 83RQ00DBVN (Core 3-304/ 8GB/ 512GB/ 14" WUXGA/ Win 11)',
     N'Lenovo IdeaPad Slim 3 14 inch, thiet ke gon nhe, RAM 8GB, SSD 512GB, Windows 11.',
     N'https://cdn.hstatic.net/products/200000722513/eapad-slim-3-14iwc11-83rq00dbvn-core-3-304-8gb-512gb-14-wuxga-win-11-1_fadbff59817145e19ec67d6942880bb3.jpg',
     24990000, 8, 1, DATEADD(SECOND, -2, @baseTime), @categoryId),
    (N'Laptop Lenovo IdeaPad Slim 3 16AHP10 83KB005YVN (Ryzen 5-125/ 8GB/ 512GB/ 16" WUXGA/ Win 11)',
     N'Lenovo IdeaPad Slim 3 16 inch, AMD Ryzen 5, RAM 8GB, SSD 512GB, Windows 11.',
     N'https://cdn.hstatic.net/products/200000722513/apad-slim-3-16ahp10-83kb005yvn-ryzen-5-125-8gb-512gb-16-wuxga-win-11-1_e865235aa29b44f8bbd532753313cf7b.jpg',
     25990000, 8, 1, DATEADD(SECOND, -1, @baseTime), @categoryId),
    (N'Laptop gaming Lenovo LOQ 15IRH11E 83Y9003WVN (Core 5-205H/ RTX 3050 6GB/ 16GB/ 512GB/ 15.3" WUXGA 165Hz/ Win 11)',
     N'Lenovo LOQ gaming, Core 5, RTX 3050, RAM 16GB, SSD 512GB, man hinh 165Hz.',
     N'https://cdn.hstatic.net/products/200000722513/y9003wvn-core-5-205h-rtx-3050-6gb-16gb-512gb-15-3-wuxga-165hz-win-11-1_791f7f3e5ff8495682527bc8c0608779.jpg',
     41990000, 4, 1, @baseTime, @categoryId);

COMMIT TRANSACTION;

SELECT ProductId, ProductName, Price, Stock, Status, CategoryId, Image
FROM products
ORDER BY CreatedAt DESC, ProductId DESC;
