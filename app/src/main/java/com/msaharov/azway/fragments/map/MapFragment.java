package com.msaharov.azway.fragments.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.msaharov.azway.R;
import com.msaharov.azway.databinding.FragmentMapBinding;
import com.msaharov.azway.models.CustomPoint;
import com.msaharov.azway.models.Mark;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.ScreenPoint;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.location.FilteringMode;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.CameraListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CameraUpdateReason;
import com.yandex.mapkit.map.Cluster;
import com.yandex.mapkit.map.ClusterListener;
import com.yandex.mapkit.map.ClusterTapListener;
import com.yandex.mapkit.map.ClusterizedPlacemarkCollection;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.MapWindow;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.RotationType;
import com.yandex.runtime.image.ImageProvider;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.processors.PublishProcessor;


public class MapFragment extends Fragment implements ClusterListener, MapObjectTapListener, ClusterTapListener {

    private MapViewModel mViewModel;
    private MapWindow mapWindow;
    private FragmentMapBinding binding;


    final CameraListener listener = new CameraListener() {
        @Override
        public void onCameraPositionChanged(@NonNull Map map, @NonNull CameraPosition cameraPosition, @NonNull CameraUpdateReason cameraUpdateReason, boolean finished) {
            if (finished) {
                mViewModel.cameraPositionLD.setValue(cameraPosition);
                mViewModel.getMarksInZone(mapWindow.screenToWorld(new ScreenPoint(0, 0)), mapWindow.screenToWorld(new ScreenPoint(
                        mapWindow.width(), mapWindow.height())));
            }

        }
    };
    private ActivityResultLauncher<String> requestPermissionLauncher;


    private Bundle filters;

    public static double startLon = 37.615560;
    public static double startLat = 55.752220;
    public static float startZoom = 10.0f;
    public static float startAnimationDuration = 1;

    private static final float FONT_SIZE = 20;
    private static final float MARGIN_SIZE = 3;
    private static final float STROKE_SIZE = 5;
    ClusterizedPlacemarkCollection clusterizedCollection;
    FragmentManager fragmentManager;
    PlacemarkMapObject userLocationPlacemark;
    LinearLayout loadingLL;
    CustomPoint userLocation;
    private com.google.android.gms.location.FusedLocationProviderClient fusedLocationClientGoogle;
    private com.huawei.hms.location.FusedLocationProviderClient fusedLocationClientHuawei;


    Dialog dialog;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(getLayoutInflater(), container, false);
        mViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initListeners();
        initUserLocation();
        moveMap(false);
        binding.mapview.getMap().setRotateGesturesEnabled(false);
    }

    protected synchronized void moveMap(boolean moveToUserLocation) {
        boolean mapMoved = false;
        Random r = new Random();
        float randomLat = -0.0150f + r.nextFloat() * (0.0150f);
        float randomLon = -0.0150f + r.nextFloat() * (0.0150f);
        startLat += randomLat;
        startLon += randomLon;

        if (!moveToUserLocation) {
            if (getArguments() != null) {
                binding.mapview.getMap().move(
                        new CameraPosition(new Point(getArguments().getDouble("latitude"),
                                getArguments().getDouble("longitude")), 18, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 0.5F),
                        null);
                mapMoved = true;
                mViewModel.visibilityOfLoadingLD.setValue(false);
            }
            if (mViewModel.cameraPositionLD.getValue() != null) {
                if (!mapMoved) {
                    binding.mapview.getMap().move(mViewModel.cameraPositionLD.getValue());
                    mapMoved = true;
                    mViewModel.visibilityOfLoadingLD.setValue(false);
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (isGPSEnabled()) {
                boolean finalMapMoved = mapMoved;
                if (areGoogleServicesAvailable()) {
                    new FusedLocationProviderClient(getContext()).getLastLocation()
                            .addOnSuccessListener(location -> {
                                if (location != null) {
                                    if (!finalMapMoved) {
                                        binding.mapview.getMap().move(
                                                new CameraPosition(new Point(location.getLatitude(),
                                                        location.getLongitude()), 16, 0.0f, 0.0f),
                                                new Animation(Animation.Type.SMOOTH, startAnimationDuration),
                                                null);
                                    }
                                    loadingLL.setVisibility(View.GONE);
                                    userLocationPlacemark.setGeometry(new Point(location.getLatitude(),
                                            location.getLongitude()));
                                    userLocation = new CustomPoint(location);
                                    userLocationPlacemark.setVisible(true);
                                } else {
                                    binding.mapview.getMap().move(
                                            new CameraPosition(new Point(startLat, startLon),
                                                    startZoom, 0.0f, 0.0f),
                                            new Animation(Animation.Type.SMOOTH, startAnimationDuration),
                                            null);
                                }
                            })
                            .addOnFailureListener(e -> {
                                if (!finalMapMoved) {
                                    binding.mapview.getMap().move(
                                            new CameraPosition(new Point(startLat, startLon),
                                                    startZoom, 0.0f, 0.0f),
                                            new Animation(Animation.Type.SMOOTH, startAnimationDuration),
                                            null);
                                }
                                mViewModel.visibilityOfLoadingLD.setValue(false);
                            });
                } else {
                    fusedLocationClientHuawei.getLastLocation()
                            .addOnSuccessListener(location -> {
                                if (location != null) {
                                    if (!finalMapMoved) {
                                        binding.mapview.getMap().move(
                                                new CameraPosition(new Point(location.getLatitude(),
                                                        location.getLongitude()), 16, 0.0f, 0.0f),
                                                new Animation(Animation.Type.SMOOTH, startAnimationDuration),
                                                null);
                                    }
                                    loadingLL.setVisibility(View.GONE);
                                    userLocationPlacemark.setGeometry(new Point(location.getLatitude(),
                                            location.getLongitude()));
                                    userLocation = new CustomPoint(location);
                                    userLocationPlacemark.setVisible(true);
                                } else {
                                    binding.mapview.getMap().move(
                                            new CameraPosition(new Point(startLat, startLon),
                                                    startZoom, 0.0f, 0.0f),
                                            new Animation(Animation.Type.SMOOTH, startAnimationDuration),
                                            null);
                                }
                            })
                            .addOnFailureListener(e -> {
                                if (!finalMapMoved) {
                                    binding.mapview.getMap().move(
                                            new CameraPosition(new Point(startLat, startLon),
                                                    startZoom, 0.0f, 0.0f),
                                            new Animation(Animation.Type.SMOOTH, startAnimationDuration),
                                            null);
                                }
                                mViewModel.visibilityOfLoadingLD.setValue(false);
                            });
                }
            } else {
                binding.mapview.getMap().move(
                        new CameraPosition(new Point(startLat, startLon),
                                startZoom, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, startAnimationDuration),
                        null);
                mViewModel.visibilityOfLoadingLD.setValue(false);
                Toast.makeText(getContext(), "Включите геолокацию для лучшей работы приложения", Toast.LENGTH_SHORT).show();
            }

        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void initUserLocation() {
        MapKit mapKit = MapKitFactory.getInstance();
        ImageProvider imageProvider = ImageProvider.fromResource(
                getActivity(), R.drawable.user_posititon_icon);
        userLocationPlacemark = binding.mapview.getMap().getMapObjects().addPlacemark(new Point(startLat, startLon));
        userLocationPlacemark.setVisible(false);
        if (userLocation != null) mViewModel.visibilityOfLoadingLD.setValue(false);

        mapKit.createLocationManager().subscribeForLocationUpdates(0, 0, 0, true, FilteringMode.ON, new LocationListener() {
            @Override
            public void onLocationUpdated(@NonNull Location location) {
                mViewModel.visibilityOfLoadingLD.setValue(false);
                userLocationPlacemark.setGeometry(location.getPosition());
                userLocationPlacemark.setVisible(true);
                userLocation = new CustomPoint(location.getPosition());
            }

            @Override
            public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {

            }
        });
    }

    private void init() {
        clusterizedCollection = binding.mapview.getMap().getMapObjects().addClusterizedPlacemarkCollection(this);
        fragmentManager = requireActivity().getSupportFragmentManager();
        fusedLocationClientGoogle = com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(getContext());
        fusedLocationClientHuawei = com.huawei.hms.location.LocationServices.getFusedLocationProviderClient(getContext());
        mapWindow = binding.mapview.getMapWindow();
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                result -> {
                    if (result) {
                        moveMap(true);
                    } else {
                        Toast.makeText(getContext(), "Дайте доступ к геопозиции", Toast.LENGTH_SHORT).show();
                        binding.mapview.getMap().move(
                                new CameraPosition(new Point(startLat, startLon),
                                        startZoom, 0.0f, 0.0f),
                                new Animation(Animation.Type.SMOOTH, startAnimationDuration),
                                null);
                        mViewModel.visibilityOfLoadingLD.setValue(false);
                    }
                }
        );
    }


    @Override
    public boolean onClusterTap(@NonNull Cluster cluster) {
        binding.mapview.getMap().move(
                new CameraPosition(cluster.getAppearance().getGeometry(), binding.mapview.getMap().getCameraPosition().getZoom() + 5, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, (float) 0.8),
                null);
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListeners() {
        mViewModel.marks.setValue(new ArrayList<>());
        binding.zoomPlusBTN.setOnClickListener(view -> binding.mapview.getMap().move(
                new CameraPosition(binding.mapview.getMap().getCameraPosition().getTarget(), binding.mapview.getMap().getCameraPosition().getZoom() + 1, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, (float) 0.1),
                null));
        binding.zoomMinusBTN.setOnClickListener(view -> binding.mapview.getMap().move(
                new CameraPosition(binding.mapview.getMap().getCameraPosition().getTarget(),
                        binding.mapview.getMap().getCameraPosition().getZoom() - 1,
                        0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, (float) 0.1),
                null));

        binding.locationBTN.setOnClickListener(view -> {
            getCurrentLocation();
        });


        mViewModel.marks.observe(getViewLifecycleOwner(), this::addPointsOnMap);

        getParentFragmentManager().setFragmentResultListener("markKey", getViewLifecycleOwner(), (requestKey, result) -> {
            Mark mark = (Mark) result.getSerializable("mark");
            binding.mapview.getMap().move(
                    new CameraPosition(new Point(mark.getLat(), mark.getLon()), 18f, 0.0f, 0.0f),
                    new Animation(Animation.Type.SMOOTH, (float) 0.8), null);
        });

        binding.mapview.getMap().addCameraListener(listener);
        mViewModel.visibilityOfLoadingLD.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean)
                loadingLL.setVisibility(View.VISIBLE);
            else {
                loadingLL.setVisibility(View.GONE);
            }
        });
        binding.mapToListBTN.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_mapFragment_to_marksListFragment);
        });


    }

    private void addPointsOnMap(ArrayList<Mark> marks) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        clusterizedCollection.clear();
        executor.execute(() ->
                Observable.fromIterable(marks)
                        .flatMap(this::addPointOnMap)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                url -> {
                                },
                                Throwable::printStackTrace,
                                () -> {
                                    Log.d("Все метки загружены: ", marks.size() + "");
                                    clusterizedCollection.clusterPlacemarks(70, 20);

                                }
                        ));
    }

    private Observable<Void> addPointOnMap(Mark mark) {
        PublishProcessor<Void> myDelayedObservable = PublishProcessor.create();
        try {
            Glide.with(this)
                    .asBitmap()
                    .load(mark.getCategory().getPinUrl())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            ImageProvider imageProvider = ImageProvider.fromBitmap(resource);
                            PlacemarkMapObject mapObject = clusterizedCollection.addPlacemark(new Point(mark.getLat(), mark.getLon()));
                            mapObject.setUserData(mark);
                            mapObject.setIcon(imageProvider, new IconStyle().setAnchor(
                                            new PointF(0.5f, 1f)).
                                    setRotationType(RotationType.NO_ROTATION)
                                    .setZIndex(0f)
                                    .setScale(1.25f));
                            mapObject.addTapListener(MapFragment.this);
                            myDelayedObservable.onComplete();
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });


        } catch (NullPointerException e) {
            e.printStackTrace();
            myDelayedObservable.onComplete();
        }
        return Observable.fromPublisher(myDelayedObservable);
    }

    private void getCurrentLocation() {
        if (userLocationPlacemark != null && userLocation != null) {
            binding.mapview.getMap().move(
                    new CameraPosition(userLocationPlacemark.getGeometry(), 18f, 0.0f, 0.0f),
                    new Animation(Animation.Type.SMOOTH, (float) 0.1),
                    null);
            return;
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (isGPSEnabled()) {
                moveMap(true);
            } else {
                Toast.makeText(getContext(), "Включите геолокацию для лучшей работы приложения", Toast.LENGTH_SHORT).show();
            }

        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }


    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled;

        if (locationManager == null) {
            locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;
    }

    @SuppressLint({"SetTextI18n", "CheckResult"})
    @Override
    public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
        try {
            Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_mark);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
            dialog.setCancelable(true);
            Mark mark = (Mark) mapObject.getUserData();
            TextView descTV = dialog.findViewById(R.id.descTV);
            TextView nameTV = dialog.findViewById(R.id.nameTV);
            ImageView mainPhotoIV = dialog.findViewById(R.id.mainPhotoIV);
            descTV.setText(mark.getDesc());
            nameTV.setText(mark.getName());
            if (mark.getImages().isEmpty()) {
                mark.getImages().add("https://i.pinimg.com/originals/15/23/86/152386d3699721e62b78c5fe2960b547.jpg");
            }
            Glide.with(getContext())
                    .load(mark.getImages().get(0))
                    .into(mainPhotoIV);
            if (getActivity() != null && !getActivity().isFinishing()) {
                dialog.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public class TextImageProvider extends ImageProvider {
        @Override
        public String getId() {
            return "text_" + text;
        }

        private final String text;

        @Override
        public Bitmap getImage() {
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager manager = (WindowManager) requireActivity().getSystemService(Context.WINDOW_SERVICE);
            manager.getDefaultDisplay().getMetrics(metrics);
            Paint textPaint = new Paint();
            textPaint.setTextSize(FONT_SIZE * metrics.density);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setColor(Color.WHITE);
            textPaint.setAntiAlias(true);
            float widthF = textPaint.measureText(text);
            Paint.FontMetrics textMetrics = textPaint.getFontMetrics();
            float heightF = Math.abs(textMetrics.bottom) + Math.abs(textMetrics.top);
            float textRadius = (float) Math.sqrt(widthF * widthF + heightF * heightF) / 2;
            float internalRadius = textRadius + MARGIN_SIZE * metrics.density;
            float externalRadius = internalRadius + STROKE_SIZE * metrics.density;
            int width = (int) (2 * externalRadius + 0.5);
            Bitmap bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Paint backgroundPaint = new Paint();
            backgroundPaint.setAntiAlias(true);
            backgroundPaint.setColor(Color.WHITE);
            canvas.drawCircle((float) width / 2, (float) width / 2, externalRadius, backgroundPaint);
            backgroundPaint.setColor(getResources().getColor(R.color.colorAccent));
            canvas.drawCircle((float) width / 2, (float) width / 2, internalRadius, backgroundPaint);
            canvas.drawText(
                    text,
                    (float) width / 2,
                    (float) width / 2 - (textMetrics.ascent + textMetrics.descent) / 2,
                    textPaint);
            return bitmap;
        }

        public TextImageProvider(String text) {
            int number = Integer.parseInt(text);
            if (number >= 10) this.text = "10+";
            else if (number >= 5) this.text = "5+";
            else this.text = text;
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        MapKitFactory.getInstance().onStop();
        binding.mapview.onStop();
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        binding.mapview.onStart();
    }

    private boolean areGoogleServicesAvailable() {
        int availability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());
        return availability == ConnectionResult.SUCCESS;

    }

    @Override
    public void onClusterAdded(Cluster cluster) {
        cluster.getPlacemarks().get(0).getDirection();
        cluster.getAppearance().setIcon(new TextImageProvider(Integer.toString(cluster.getSize())));
        cluster.addClusterTapListener(this);
    }
}