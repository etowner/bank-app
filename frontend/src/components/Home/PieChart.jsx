import "chart.js/auto";
import { Pie } from "react-chartjs-2";

export default function PieChart({ accounts }) {
  const totalBalance = accounts.reduce((sum, account) => sum + account.balance, 0);

  const percentages = accounts.map(account => 
    totalBalance > 0 ? (account.balance / totalBalance) * 100 : 0
  );

  const data = {
    labels: accounts.map((account) => `${account.type} - ${account.accountNumber}`),
    datasets: [
      {
        label: "%",
        data: percentages,
        backgroundColor: [
          "rgba(40, 44, 52, 1)",
          "rgba(239, 196, 92, 1)",
          "rgba(112, 128, 144, 1)",
        ],
        hoverOffset: 4,
      },
    ],
  };

  const options = {
    plugins: {
      legend: {
        display: true,
        position: "bottom",
      },
    },
    layout: {
      padding: 20,
      backgroundColor: "rgba(40, 44, 52, 1)",
    },
    elements: {
      backgroundColor: "rgba(40, 44, 52, 1)",
    },
  };

  return (
    <div>
      <Pie data={data} options={options} />
    </div>
  );
}
