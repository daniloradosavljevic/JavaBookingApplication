package booking.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * Main BookingService
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.57.2)",
    comments = "Source: booking.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class BookingServiceGrpc {

  private BookingServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "booking.grpc.BookingService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<booking.grpc.SearchRequest,
      booking.grpc.SearchResponse> getSearchHotelsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SearchHotels",
      requestType = booking.grpc.SearchRequest.class,
      responseType = booking.grpc.SearchResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<booking.grpc.SearchRequest,
      booking.grpc.SearchResponse> getSearchHotelsMethod() {
    io.grpc.MethodDescriptor<booking.grpc.SearchRequest, booking.grpc.SearchResponse> getSearchHotelsMethod;
    if ((getSearchHotelsMethod = BookingServiceGrpc.getSearchHotelsMethod) == null) {
      synchronized (BookingServiceGrpc.class) {
        if ((getSearchHotelsMethod = BookingServiceGrpc.getSearchHotelsMethod) == null) {
          BookingServiceGrpc.getSearchHotelsMethod = getSearchHotelsMethod =
              io.grpc.MethodDescriptor.<booking.grpc.SearchRequest, booking.grpc.SearchResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SearchHotels"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  booking.grpc.SearchRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  booking.grpc.SearchResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BookingServiceMethodDescriptorSupplier("SearchHotels"))
              .build();
        }
      }
    }
    return getSearchHotelsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<booking.grpc.ReservationRequest,
      booking.grpc.ReservationResponse> getMakeReservationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "MakeReservation",
      requestType = booking.grpc.ReservationRequest.class,
      responseType = booking.grpc.ReservationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<booking.grpc.ReservationRequest,
      booking.grpc.ReservationResponse> getMakeReservationMethod() {
    io.grpc.MethodDescriptor<booking.grpc.ReservationRequest, booking.grpc.ReservationResponse> getMakeReservationMethod;
    if ((getMakeReservationMethod = BookingServiceGrpc.getMakeReservationMethod) == null) {
      synchronized (BookingServiceGrpc.class) {
        if ((getMakeReservationMethod = BookingServiceGrpc.getMakeReservationMethod) == null) {
          BookingServiceGrpc.getMakeReservationMethod = getMakeReservationMethod =
              io.grpc.MethodDescriptor.<booking.grpc.ReservationRequest, booking.grpc.ReservationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "MakeReservation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  booking.grpc.ReservationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  booking.grpc.ReservationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BookingServiceMethodDescriptorSupplier("MakeReservation"))
              .build();
        }
      }
    }
    return getMakeReservationMethod;
  }

  private static volatile io.grpc.MethodDescriptor<booking.grpc.PaymentRequest,
      booking.grpc.PaymentResponse> getPayForReservationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "PayForReservation",
      requestType = booking.grpc.PaymentRequest.class,
      responseType = booking.grpc.PaymentResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<booking.grpc.PaymentRequest,
      booking.grpc.PaymentResponse> getPayForReservationMethod() {
    io.grpc.MethodDescriptor<booking.grpc.PaymentRequest, booking.grpc.PaymentResponse> getPayForReservationMethod;
    if ((getPayForReservationMethod = BookingServiceGrpc.getPayForReservationMethod) == null) {
      synchronized (BookingServiceGrpc.class) {
        if ((getPayForReservationMethod = BookingServiceGrpc.getPayForReservationMethod) == null) {
          BookingServiceGrpc.getPayForReservationMethod = getPayForReservationMethod =
              io.grpc.MethodDescriptor.<booking.grpc.PaymentRequest, booking.grpc.PaymentResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "PayForReservation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  booking.grpc.PaymentRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  booking.grpc.PaymentResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BookingServiceMethodDescriptorSupplier("PayForReservation"))
              .build();
        }
      }
    }
    return getPayForReservationMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static BookingServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BookingServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BookingServiceStub>() {
        @java.lang.Override
        public BookingServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BookingServiceStub(channel, callOptions);
        }
      };
    return BookingServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BookingServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BookingServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BookingServiceBlockingStub>() {
        @java.lang.Override
        public BookingServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BookingServiceBlockingStub(channel, callOptions);
        }
      };
    return BookingServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static BookingServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BookingServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BookingServiceFutureStub>() {
        @java.lang.Override
        public BookingServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BookingServiceFutureStub(channel, callOptions);
        }
      };
    return BookingServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * Main BookingService
   * </pre>
   */
  public interface AsyncService {

    /**
     */
    default void searchHotels(booking.grpc.SearchRequest request,
        io.grpc.stub.StreamObserver<booking.grpc.SearchResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSearchHotelsMethod(), responseObserver);
    }

    /**
     */
    default void makeReservation(booking.grpc.ReservationRequest request,
        io.grpc.stub.StreamObserver<booking.grpc.ReservationResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getMakeReservationMethod(), responseObserver);
    }

    /**
     */
    default void payForReservation(booking.grpc.PaymentRequest request,
        io.grpc.stub.StreamObserver<booking.grpc.PaymentResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPayForReservationMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service BookingService.
   * <pre>
   * Main BookingService
   * </pre>
   */
  public static abstract class BookingServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return BookingServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service BookingService.
   * <pre>
   * Main BookingService
   * </pre>
   */
  public static final class BookingServiceStub
      extends io.grpc.stub.AbstractAsyncStub<BookingServiceStub> {
    private BookingServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BookingServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BookingServiceStub(channel, callOptions);
    }

    /**
     */
    public void searchHotels(booking.grpc.SearchRequest request,
        io.grpc.stub.StreamObserver<booking.grpc.SearchResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSearchHotelsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void makeReservation(booking.grpc.ReservationRequest request,
        io.grpc.stub.StreamObserver<booking.grpc.ReservationResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getMakeReservationMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void payForReservation(booking.grpc.PaymentRequest request,
        io.grpc.stub.StreamObserver<booking.grpc.PaymentResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPayForReservationMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service BookingService.
   * <pre>
   * Main BookingService
   * </pre>
   */
  public static final class BookingServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<BookingServiceBlockingStub> {
    private BookingServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BookingServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BookingServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public booking.grpc.SearchResponse searchHotels(booking.grpc.SearchRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSearchHotelsMethod(), getCallOptions(), request);
    }

    /**
     */
    public booking.grpc.ReservationResponse makeReservation(booking.grpc.ReservationRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getMakeReservationMethod(), getCallOptions(), request);
    }

    /**
     */
    public booking.grpc.PaymentResponse payForReservation(booking.grpc.PaymentRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPayForReservationMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service BookingService.
   * <pre>
   * Main BookingService
   * </pre>
   */
  public static final class BookingServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<BookingServiceFutureStub> {
    private BookingServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BookingServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BookingServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<booking.grpc.SearchResponse> searchHotels(
        booking.grpc.SearchRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSearchHotelsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<booking.grpc.ReservationResponse> makeReservation(
        booking.grpc.ReservationRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getMakeReservationMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<booking.grpc.PaymentResponse> payForReservation(
        booking.grpc.PaymentRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getPayForReservationMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SEARCH_HOTELS = 0;
  private static final int METHODID_MAKE_RESERVATION = 1;
  private static final int METHODID_PAY_FOR_RESERVATION = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SEARCH_HOTELS:
          serviceImpl.searchHotels((booking.grpc.SearchRequest) request,
              (io.grpc.stub.StreamObserver<booking.grpc.SearchResponse>) responseObserver);
          break;
        case METHODID_MAKE_RESERVATION:
          serviceImpl.makeReservation((booking.grpc.ReservationRequest) request,
              (io.grpc.stub.StreamObserver<booking.grpc.ReservationResponse>) responseObserver);
          break;
        case METHODID_PAY_FOR_RESERVATION:
          serviceImpl.payForReservation((booking.grpc.PaymentRequest) request,
              (io.grpc.stub.StreamObserver<booking.grpc.PaymentResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getSearchHotelsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              booking.grpc.SearchRequest,
              booking.grpc.SearchResponse>(
                service, METHODID_SEARCH_HOTELS)))
        .addMethod(
          getMakeReservationMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              booking.grpc.ReservationRequest,
              booking.grpc.ReservationResponse>(
                service, METHODID_MAKE_RESERVATION)))
        .addMethod(
          getPayForReservationMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              booking.grpc.PaymentRequest,
              booking.grpc.PaymentResponse>(
                service, METHODID_PAY_FOR_RESERVATION)))
        .build();
  }

  private static abstract class BookingServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    BookingServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return booking.grpc.BookingProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("BookingService");
    }
  }

  private static final class BookingServiceFileDescriptorSupplier
      extends BookingServiceBaseDescriptorSupplier {
    BookingServiceFileDescriptorSupplier() {}
  }

  private static final class BookingServiceMethodDescriptorSupplier
      extends BookingServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    BookingServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (BookingServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new BookingServiceFileDescriptorSupplier())
              .addMethod(getSearchHotelsMethod())
              .addMethod(getMakeReservationMethod())
              .addMethod(getPayForReservationMethod())
              .build();
        }
      }
    }
    return result;
  }
}
