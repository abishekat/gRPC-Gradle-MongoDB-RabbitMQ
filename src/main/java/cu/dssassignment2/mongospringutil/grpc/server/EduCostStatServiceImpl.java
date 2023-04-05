package cu.dssassignment2.mongospringutil.grpc.server;

import cu.assignment2.proto.*;
import cu.dssassignment2.mongospringutil.model.EduCostStat;
import cu.dssassignment2.mongospringutil.model.*;
import cu.dssassignment2.mongospringutil.repository.*;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EduCostStatServiceImpl extends EduCostStatServiceGrpc.EduCostStatServiceImplBase {
    private final EduCostStatRepository eduCostStatRepository;
    @Autowired
    EduCostStatQueryOneRepository eduCostStatQueryOneRepository;
    @Autowired
    EduCostStatQueryTwoRepository eduCostStatQueryTwoRepository;
    @Autowired
    EduCostStatQueryThreeRepository eduCostStatQueryThreeRepository;
    @Autowired
    EduCostStatQueryFourRepository eduCostStatQueryFourRepository;
    @Autowired
    EduCostStatQueryFiveRepository eduCostStatQueryFiveRepository;

    @Autowired
    public EduCostStatServiceImpl(EduCostStatRepository eduCostStatRepository) {
        this.eduCostStatRepository = eduCostStatRepository;
    }

    @Override
    public void test(Request request, StreamObserver<Response> responseObserver) {
        System.out.println("Received request with name: " + request.getName());
        Response response = Response.newBuilder().setName("Hello, " + request.getName()).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void q1(QueryRequest request, StreamObserver<QueryResponse> responseObserver) {
        List<EduCostStat> results = eduCostStatRepository.findByYearAndStateAndTypeAndLengthAndExpense(request.getYear(), request.getState(),
                request.getType(), request.getLength(), request.getExpense());

        List<EduCostStatQueryOne> q1Results = new ArrayList<>();
        Set<String> uniqueRecords = new HashSet<>();
        for (EduCostStat eduCostStat : results) {
            String key = String.format("%s-%s-%s-%s-%s-%s", eduCostStat.getYear(), eduCostStat.getState(),
                    eduCostStat.getType(), eduCostStat.getLength(), eduCostStat.getExpense(), eduCostStat.getValue());

            if (!uniqueRecords.contains(key)) {
                EduCostStatQueryOne queryOne = new EduCostStatQueryOne();
                queryOne.setId(eduCostStat.getId());
                queryOne.setYear(eduCostStat.getYear());
                queryOne.setState(eduCostStat.getState());
                queryOne.setType(eduCostStat.getType());
                queryOne.setLength(eduCostStat.getLength());
                queryOne.setExpense(eduCostStat.getExpense());
                queryOne.setValue(eduCostStat.getValue());
                q1Results.add(queryOne);
                uniqueRecords.add(key);
            }
        }
        eduCostStatQueryOneRepository.saveAll(q1Results);
        QueryResponse.Builder responseBuilder = QueryResponse.newBuilder();
        for (EduCostStatQueryOne eduCostStat : q1Results) {
            cu.assignment2.proto.EduCostStat.Builder eduCostStatBuilder = cu.assignment2.proto.EduCostStat.newBuilder();
            eduCostStatBuilder.setId(eduCostStat.getId());
            eduCostStatBuilder.setYear(eduCostStat.getYear());
            eduCostStatBuilder.setState(eduCostStat.getState());
            eduCostStatBuilder.setType(eduCostStat.getType());
            eduCostStatBuilder.setLength(eduCostStat.getLength());
            eduCostStatBuilder.setExpense(eduCostStat.getExpense());
            eduCostStatBuilder.setValue(eduCostStat.getValue());

            responseBuilder.addEduCostStats(eduCostStatBuilder.build());
        }
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void q2(Query2Request request, StreamObserver<QueryResponse> responseObserver) {
        List<EduCostStat> results = eduCostStatRepository.findByYearAndTypeAndLength(request.getYear(),
                request.getType(), request.getLength());
        Map<String, Double> stateExpensesMap = new HashMap<>();
        for (EduCostStat eduCostStat : results) {
            String state = eduCostStat.getState();
            Double expense = Double.parseDouble(eduCostStat.getValue());
            if (stateExpensesMap.containsKey(state)) {
                expense += stateExpensesMap.get(state);
            }
            stateExpensesMap.put(state, expense);
        }

        List<Map.Entry<String, Double>> sortedList = stateExpensesMap.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(5)
                .collect(Collectors.toList());

        List<EduCostStatQueryTwo> q2Results = new ArrayList<>();
        for (Map.Entry<String, Double> entry : sortedList) {
            EduCostStatQueryTwo queryTwo = new EduCostStatQueryTwo();
            queryTwo.setYear(request.getYear());
            queryTwo.setType(request.getType());
            queryTwo.setLength(request.getLength());
            queryTwo.setState(entry.getKey());
            queryTwo.setExpense(entry.getValue().toString());
            q2Results.add(queryTwo);
        }

        eduCostStatQueryTwoRepository.saveAll(q2Results);

        QueryResponse.Builder responseBuilder = QueryResponse.newBuilder();
        for (EduCostStatQueryTwo eduCostStat : q2Results) {
            cu.assignment2.proto.EduCostStat.Builder eduCostStatBuilder = cu.assignment2.proto.EduCostStat.newBuilder();
            eduCostStatBuilder.setId(eduCostStat.getId());
            eduCostStatBuilder.setYear(eduCostStat.getYear());
            eduCostStatBuilder.setState(eduCostStat.getState());
            eduCostStatBuilder.setType(eduCostStat.getType());
            eduCostStatBuilder.setLength(eduCostStat.getLength());
            eduCostStatBuilder.setExpense(eduCostStat.getExpense());
            responseBuilder.addEduCostStats(eduCostStatBuilder.build());
        }
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void q3(Query3Request request, StreamObserver<QueryResponse> responseObserver) {
        List<EduCostStat> results = eduCostStatRepository.findByYearAndTypeAndLengthAndExpense(request.getYear(), request.getType(), request.getLength(), request.getExpense());

        // Group the results by state and sum up the expenses
        Map<String, Double> stateExpenses = new HashMap<>();
        for (EduCostStat eduCostStat : results) {
            String state = eduCostStat.getState();
            double expense = Double.parseDouble(eduCostStat.getValue());
            stateExpenses.put(state, stateExpenses.getOrDefault(state, 0.0) + expense);
        }

        // Sort the states by total expense in ascending order and limit the results to the top 5
        List<Map.Entry<String, Double>> sortedStateExpenses = stateExpenses.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(5)
                .collect(Collectors.toList());

        List<EduCostStatQueryThree> q3Results = new ArrayList<>();
        for (Map.Entry<String, Double> entry : sortedStateExpenses) {
            EduCostStatQueryThree queryThree = new EduCostStatQueryThree();
            queryThree.setYear(request.getYear());
            queryThree.setType(request.getType());
            queryThree.setLength(request.getLength());
            queryThree.setExpense(request.getExpense());
            queryThree.setState(entry.getKey());
            queryThree.setExpense(entry.getValue().toString());
            q3Results.add(queryThree);
        }
        eduCostStatQueryThreeRepository.saveAll(q3Results);
        QueryResponse.Builder responseBuilder = QueryResponse.newBuilder();
        for (EduCostStatQueryThree eduCostStat : q3Results) {
            cu.assignment2.proto.EduCostStat.Builder eduCostStatBuilder = cu.assignment2.proto.EduCostStat.newBuilder();
            eduCostStatBuilder.setId(eduCostStat.getId());
            eduCostStatBuilder.setYear(eduCostStat.getYear());
            eduCostStatBuilder.setState(eduCostStat.getState());
            eduCostStatBuilder.setType(eduCostStat.getType());
            eduCostStatBuilder.setLength(eduCostStat.getLength());
            eduCostStatBuilder.setExpense(eduCostStat.getExpense());
            responseBuilder.addEduCostStats(eduCostStatBuilder.build());
        }
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();

    }

    @Override
    public void q4(Query4Request request, StreamObserver<QueryResponse> responseObserver) {

        int baseYear = Integer.parseInt(request.getYear());
        int[] pastYears = {baseYear - 1, baseYear - 3, baseYear - 5};

        List<EduCostStat> baseYearStats = eduCostStatRepository.findByYearAndTypeAndLength(request.getYear(),
                request.getType(), request.getLength());
        Map<String, Integer> baseYearStateExpenses = new HashMap<>();
        for (EduCostStat eduCostStat : baseYearStats) {
            String state = eduCostStat.getState();
            int expense = Integer.parseInt(eduCostStat.getValue());
            baseYearStateExpenses.put(state, expense);
        }

        Map<Integer, Map<String, Integer>> pastYearStateExpenses = new HashMap<>();
        for (int year : pastYears) {
            List<EduCostStat> pastYearStats = eduCostStatRepository.findByYearAndTypeAndLength(String.valueOf(year),
                    request.getType(), request.getLength());
            Map<String, Integer> pastYearStateExpense = new HashMap<>();
            for (EduCostStat eduCostStat : pastYearStats) {
                String state = eduCostStat.getState();
                int expense = Integer.parseInt(eduCostStat.getValue());
                pastYearStateExpense.put(state, expense);
            }
            pastYearStateExpenses.put(year, pastYearStateExpense);
        }

        Map<String, Double> stateGrowthRates = new HashMap<>();
        for (String state : baseYearStateExpenses.keySet()) {
            double growthRate = 0.0;
            int baseYearExpense = baseYearStateExpenses.get(state);
            for (int year : pastYears) {
                if (pastYearStateExpenses.containsKey(year)) {
                    Map<String, Integer> pastYearStateExpense = pastYearStateExpenses.get(year);
                    if (pastYearStateExpense.containsKey(state)) {
                        int pastYearExpense = pastYearStateExpense.get(state);
                        growthRate += (double) (baseYearExpense - pastYearExpense) / (double) baseYearExpense;
                    }
                }
            }
            stateGrowthRates.put(state, growthRate);
        }

        List<Map.Entry<String, Double>> sortedStateGrowthRates = stateGrowthRates.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toList());

        List<cu.dssassignment2.mongospringutil.model.EduCostStatQueryFour> q4Results = new ArrayList<>();

        for (Map.Entry<String, Double> entry : sortedStateGrowthRates) {
            cu.dssassignment2.mongospringutil.model.EduCostStatQueryFour queryFour = new cu.dssassignment2.mongospringutil.model.EduCostStatQueryFour();
            queryFour.setYear(String.valueOf(baseYear));
            queryFour.setPastYear(Arrays.stream(pastYears).boxed().collect(Collectors.toList()).toString());
            queryFour.setType(request.getType());
            queryFour.setLength(request.getLength());
            queryFour.setState(entry.getKey());
            queryFour.setGrowthRate(entry.getValue().toString());
            eduCostStatQueryFourRepository.save(queryFour);
        }

        List<EduCostStatQueryFour> q4SortResults= eduCostStatQueryFourRepository.findAll();
        QueryResponse.Builder responseBuilder = QueryResponse.newBuilder();
        for (EduCostStatQueryFour eduCostStat : q4SortResults) {
            cu.assignment2.proto.EduCostStat.Builder eduCostStatBuilder = cu.assignment2.proto.EduCostStat.newBuilder();
            eduCostStatBuilder.setId(eduCostStat.getId());
            eduCostStatBuilder.setYear(eduCostStat.getYear());
            eduCostStatBuilder.setState(eduCostStat.getState());
            eduCostStatBuilder.setType(eduCostStat.getType());
            eduCostStatBuilder.setLength(eduCostStat.getLength());
            eduCostStatBuilder.setExpense(eduCostStat.getExpense()==null?"0":eduCostStat.getExpense());

            responseBuilder.addEduCostStats(eduCostStatBuilder.build());
        }
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void q5(Query4Request request, StreamObserver<Query5Response> responseObserver) {
        List<EduCostStat> eduCostStats = eduCostStatRepository.findByYearAndTypeAndLength(request.getYear(), request.getType(), request.getLength());
        Map<String, List<Double>> regionExpenseMap = new HashMap<>();
        for (EduCostStat eduCostStat : eduCostStats) {
            String state = eduCostStat.getState();
            String region = getRegionByState(state);

            if (region == null) {
                continue;
            }

            List<Double> expenses = regionExpenseMap.getOrDefault(region, new ArrayList<>());
            expenses.add(Double.parseDouble(eduCostStat.getValue()));
            regionExpenseMap.put(region, expenses);
        }
        List<EduCostStatQueryFive> q5Results = new ArrayList<>();
        for (Map.Entry<String, List<Double>> entry : regionExpenseMap.entrySet()) {
            EduCostStatQueryFive q5 = new EduCostStatQueryFive();
            String region = entry.getKey();
            List<Double> expenses = entry.getValue();
            double sum = expenses.stream().mapToDouble(Double::doubleValue).sum();
            double average = sum / expenses.size();
            q5.setRegion(region);
            q5.setExpense(String.valueOf(average));
            q5Results.add(q5);
        }
        eduCostStatQueryFiveRepository.saveAll(q5Results);
        Query5Response.Builder responseBuilder = Query5Response.newBuilder();
        for (EduCostStatQueryFive eduCostStat : q5Results) {
            cu.assignment2.proto.RegionExpense.Builder eduCostStatBuilder = cu.assignment2.proto.RegionExpense.newBuilder();
            eduCostStatBuilder.setExpense(eduCostStat.getExpense());
            eduCostStatBuilder.setRegion(eduCostStat.getRegion());
            responseBuilder.addRegionExpenses(eduCostStatBuilder.build());
        }
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    private String getRegionByState(String state) {
        ArrayList<String> northeast = new ArrayList<>(Arrays.asList("Connecticut", "Maine", "Massachusetts", "New Hampshire", "New Jersey", "New York", "Pennsylvania", "Rhode Island", "Vermont"));
        ArrayList<String> midwest = new ArrayList<>(Arrays.asList("Illinois", "Indiana", "Iowa", "Kansas", "Michigan", "Minnesota", "Missouri", "Nebraska", "North Dakota", "Ohio", "South Dakota", "Wisconsin"));
        ArrayList<String> south = new ArrayList<>(Arrays.asList("Alabama", "Arkansas", "Delaware", "Florida", "Georgia", "Kentucky", "Louisiana", "Maryland", "Mississippi", "North Carolina", "Oklahoma", "South Carolina", "Tennessee", "Texas", "Virginia", "West Virginia"));
        ArrayList<String> west = new ArrayList<>(Arrays.asList("Alaska", "Arizona", "California", "Colorado", "Idaho", "Montana", "Nevada", "New Mexico", "Oregon", "Utah", "Washington", "Wyoming", "District of Columbia"));
        ArrayList<String> pacific = new ArrayList<>(Arrays.asList("Alaska", "Hawaii", "Oregon", "Washington"));

        if (northeast.contains(state)) {
            return "Northeast Region";
        } else if (midwest.contains(state)) {
            return "Midwest Region";
        } else if (south.contains(state)) {
            return "South Region";
        } else if (west.contains(state)) {
            return "West Region";
        } else if (pacific.contains(state)) {
            return "Pacific Region";
        } else {
            return "Unknown Region";
        }
    }
}