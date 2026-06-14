import "chart.js/auto";
import { Line } from "react-chartjs-2";
import { ChartOptions } from "chart.js";
import { Transaction } from "../../lib/types";
import { formatDate } from '../../lib/utils';

interface LineChartProps {
  accountNumber: string;
  transactions: Transaction[]; // Adjust type as needed based on your transaction data structure
}


export default function LineChart({ accountNumber, transactions }: LineChartProps) {
  let transactionLabels: string[] = [];
  let transactionAmounts: number[] = [];
  if (transactions && transactions.length > 0) {
    transactionLabels = transactions.map(
      (t) => formatDate(t.timestamp),
    );
    transactionAmounts = transactions.map((t) => t.amount);
  }

  const data = {
    labels: transactionLabels,
    datasets: [
      {
        label: accountNumber,
        data: transactionAmounts,
        borderColor: "rgb(112, 128, 144)",
        backgroundColor: "rgba(112, 128, 144, 0.5)",
      },
    ],
  };

  const options: ChartOptions<"line"> = {
    responsive: true,
    plugins: {
      legend: {
        display: true,
        position: "top",
      },
      title: {
        display: true,
        text: "Transaction graph",
      },
    },
  };

  
  return (
    <div>
      <Line data={data} options={options} />
    </div>
  );
}
